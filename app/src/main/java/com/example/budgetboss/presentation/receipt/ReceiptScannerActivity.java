package com.example.budgetboss.presentation.receipt;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.budgetboss.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReceiptScannerActivity extends AppCompatActivity {

    private ImageView preview;
    private MaterialTextView result;
    private ProgressBar progressBar;
    private LinearLayout placeholder;
    private MaterialButton btnUseData;
    
    private String scannedTitle = "";
    private String scannedAmount = "";
    private String scannedCategory = "";
    private Uri currentPhotoUri;
    
    private final ActivityResultLauncher<String> pickImage = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            this::handleImageSelected
    );
    
    private final ActivityResultLauncher<Uri> takePicture = registerForActivityResult(
            new ActivityResultContracts.TakePicture(),
            success -> {
                if (success && currentPhotoUri != null) {
                    handleImageSelected(currentPhotoUri);
                }
            }
    );
    
    private final ActivityResultLauncher<String> requestCameraPermission = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                if (isGranted) {
                    launchCamera();
                } else {
                    Toast.makeText(this, "Camera permission required", Toast.LENGTH_SHORT).show();
                }
            }
    );

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_scanner);

        preview = findViewById(R.id.ivReceiptPreview);
        result = findViewById(R.id.tvScanResult);
        progressBar = findViewById(R.id.progressScan);
        placeholder = findViewById(R.id.layoutPlaceholder);
        btnUseData = findViewById(R.id.btnUseData);
        
        MaterialButton btnPick = findViewById(R.id.btnPickImage);
        MaterialButton btnCamera = findViewById(R.id.btnCamera);
        MaterialButton btnClose = findViewById(R.id.btnCloseScanner);

        btnPick.setOnClickListener(v -> pickImage.launch("image/*"));
        btnCamera.setOnClickListener(v -> checkCameraPermissionAndLaunch());
        btnClose.setOnClickListener(v -> finish());
        
        btnUseData.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("scanned_title", scannedTitle);
            resultIntent.putExtra("scanned_amount", scannedAmount);
            resultIntent.putExtra("scanned_category", scannedCategory);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
    
    private void checkCameraPermissionAndLaunch() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) 
                == PackageManager.PERMISSION_GRANTED) {
            launchCamera();
        } else {
            requestCameraPermission.launch(Manifest.permission.CAMERA);
        }
    }
    
    private void launchCamera() {
        try {
            File photoFile = createImageFile();
            currentPhotoUri = FileProvider.getUriForFile(this,
                    getPackageName() + ".fileprovider", photoFile);
            takePicture.launch(currentPhotoUri);
        } catch (IOException e) {
            Toast.makeText(this, "Unable to create image file", Toast.LENGTH_SHORT).show();
        }
    }
    
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "RECEIPT_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    private void handleImageSelected(@Nullable Uri uri) {
        if (uri == null) {
            Toast.makeText(this, "No receipt selected", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            Bitmap bitmap = loadBitmapFromUri(uri);
            preview.setImageBitmap(bitmap);
            placeholder.setVisibility(View.GONE);
            runTextRecognition(uri);
        } catch (IOException e) {
            Snackbar.make(preview, "Unable to load image", Snackbar.LENGTH_LONG).show();
        }
    }

    private Bitmap loadBitmapFromUri(Uri uri) throws IOException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), uri);
            return ImageDecoder.decodeBitmap(source);
        } else {
            return MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
        }
    }

    private void runTextRecognition(Uri uri) {
        progressBar.setVisibility(View.VISIBLE);
        result.setText("Scanning receipt...");
        btnUseData.setVisibility(View.GONE);
        
        try {
            InputImage image = InputImage.fromFilePath(this, uri);
            TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
                    .process(image)
                    .addOnSuccessListener(this::onTextFound)
                    .addOnFailureListener(e -> {
                        progressBar.setVisibility(View.GONE);
                        result.setText("No text detected. Try another photo.");
                    });
        } catch (IOException e) {
            progressBar.setVisibility(View.GONE);
            result.setText("Unable to process image");
        }
    }

    private void onTextFound(Text text) {
        progressBar.setVisibility(View.GONE);
        if (text.getText().isEmpty()) {
            result.setText("No text detected. Try another photo.");
            return;
        }
        
        String fullText = text.getText();
        
        // Extract amount - look for currency patterns
        Pattern amountPattern = Pattern.compile("(?:₹|Rs\\.?|INR|Total|Amount|Grand Total)[:\\s]*([0-9,]+\\.?[0-9]*)");
        Matcher amountMatcher = amountPattern.matcher(fullText);
        if (amountMatcher.find()) {
            scannedAmount = amountMatcher.group(1).replace(",", "");
        } else {
            // Fallback - find any number that looks like a price
            Pattern fallbackAmount = Pattern.compile("([0-9]+\\.?[0-9]{0,2})");
            Matcher fallbackMatcher = fallbackAmount.matcher(fullText);
            while (fallbackMatcher.find()) {
                String num = fallbackMatcher.group(1);
                try {
                    double val = Double.parseDouble(num);
                    if (val > 10 && val < 100000) { // Reasonable price range
                        scannedAmount = num;
                    }
                } catch (NumberFormatException ignored) {}
            }
        }
        
        // Extract title - usually first meaningful line
        String[] lines = fullText.split("\n");
        for (String line : lines) {
            line = line.trim();
            if (line.length() > 3 && line.length() < 50 && !line.matches(".*[0-9]{3,}.*")) {
                scannedTitle = line;
                break;
            }
        }
        
        // Categorize based on keywords
        String lowerText = fullText.toLowerCase();
        if (lowerText.contains("restaurant") || lowerText.contains("food") || lowerText.contains("cafe") 
                || lowerText.contains("hotel") || lowerText.contains("swiggy") || lowerText.contains("zomato")) {
            scannedCategory = "Food";
        } else if (lowerText.contains("grocery") || lowerText.contains("mart") || lowerText.contains("supermarket")
                || lowerText.contains("bigbasket") || lowerText.contains("blinkit")) {
            scannedCategory = "Groceries";
        } else if (lowerText.contains("fuel") || lowerText.contains("petrol") || lowerText.contains("diesel")
                || lowerText.contains("gas station")) {
            scannedCategory = "Transport";
        } else if (lowerText.contains("amazon") || lowerText.contains("flipkart") || lowerText.contains("shopping")
                || lowerText.contains("myntra")) {
            scannedCategory = "Shopping";
        } else if (lowerText.contains("medicine") || lowerText.contains("pharmacy") || lowerText.contains("medical")
                || lowerText.contains("hospital")) {
            scannedCategory = "Healthcare";
        } else if (lowerText.contains("movie") || lowerText.contains("entertainment") || lowerText.contains("pvr")
                || lowerText.contains("inox")) {
            scannedCategory = "Entertainment";
        } else {
            scannedCategory = "Other";
        }
        
        // Display extracted info
        StringBuilder extracted = new StringBuilder("✓ Extracted Data:\n");
        if (!scannedTitle.isEmpty()) extracted.append("• ").append(scannedTitle).append("\n");
        if (!scannedAmount.isEmpty()) extracted.append("• Amount: ₹").append(scannedAmount).append("\n");
        if (!scannedCategory.isEmpty()) extracted.append("• Category: ").append(scannedCategory);
        
        result.setText(extracted.toString());
        btnUseData.setVisibility(View.VISIBLE);
    }
}
