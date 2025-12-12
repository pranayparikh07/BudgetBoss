package com.example.budgetboss.presentation.ai;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.budgetboss.R;

public class AIChatFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ai_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        android.widget.EditText etMessage = view.findViewById(R.id.etMessage);
        android.widget.Button btnSend = view.findViewById(R.id.btnSend);
        android.widget.LinearLayout chatContainer = view.findViewById(R.id.chatContainer);

        btnSend.setOnClickListener(v -> {
            String msg = etMessage.getText().toString().trim();
            if (!msg.isEmpty()) {
                // Add User Message
                addMessage(chatContainer, msg, true);
                etMessage.setText("");

                // Mock AI Response
                view.postDelayed(() -> {
                    addMessage(chatContainer,
                            "That's an interesting observation! Analysis suggests saving more in 'Entertainment' could optimize your budget.",
                            false);
                }, 1000);
            }
        });
    }

    private void addMessage(android.widget.LinearLayout container, String text, boolean isUser) {
        TextView tv = new TextView(requireContext());
        tv.setText(text);
        tv.setPadding(32, 24, 32, 24);
        tv.setTextColor(isUser ? android.graphics.Color.WHITE : android.graphics.Color.BLACK);

        android.widget.LinearLayout.LayoutParams params = new android.widget.LinearLayout.LayoutParams(
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 32);

        if (isUser) {
            params.gravity = android.view.Gravity.END;
            tv.setBackgroundTintList(android.content.res.ColorStateList.valueOf(
                    androidx.core.content.ContextCompat.getColor(requireContext(), R.color.colorPrimary)));
        } else {
            params.gravity = android.view.Gravity.START;
            tv.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFFE0E0E0));
        }

        // Simple background for now, utilizing standard drawable or color
        tv.setBackgroundResource(android.R.drawable.dialog_holo_light_frame); // Fallback shape
        // Ideally use a shape drawable, but color tinting works on some backgrounds

        tv.setLayoutParams(params);
        container.addView(tv);
    }
}
