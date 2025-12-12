package com.example.budgetboss.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SimpleLineChartView extends View {

    private Paint linePaint;
    private Paint fillPaint;
    private Path linePath;
    private List<Float> dataPoints = new ArrayList<>();

    public SimpleLineChartView(Context context) {
        super(context);
        init();
    }

    public SimpleLineChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        linePaint = new Paint();
        linePaint.setColor(Color.parseColor("#00E5FF")); // Cyan
        linePaint.setStrokeWidth(8f);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setAntiAlias(true);

        fillPaint = new Paint();
        fillPaint.setColor(Color.parseColor("#3300E5FF")); // Cyan transparent
        fillPaint.setStyle(Paint.Style.FILL);
        fillPaint.setAntiAlias(true);

        linePath = new Path();
    }

    public void setData(List<Float> data) {
        this.dataPoints = data;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (dataPoints == null || dataPoints.isEmpty())
            return;

        float width = getWidth();
        float height = getHeight();

        // Normalize data to fit height
        float max = 0;
        for (float f : dataPoints)
            if (f > max)
                max = f;
        if (max == 0)
            max = 1; // Avoid divide by zero

        float spacing = width / (dataPoints.size() - 1);

        linePath.reset();
        linePath.moveTo(0, height - (dataPoints.get(0) / max * height));

        for (int i = 1; i < dataPoints.size(); i++) {
            float x = i * spacing;
            float y = height - (dataPoints.get(i) / max * height);

            // Create cubic bezier for smooth curve
            float prevX = (i - 1) * spacing;
            float prevY = height - (dataPoints.get(i - 1) / max * height);
            float controlX1 = prevX + (x - prevX) / 2;
            float controlX2 = x - (x - prevX) / 2;

            linePath.cubicTo(controlX1, prevY, controlX2, y, x, y);
            // linePath.lineTo(x, y); // Linear
        }

        canvas.drawPath(linePath, linePaint);

        // Draw Fill
        Path fillPath = new Path(linePath);
        fillPath.lineTo(width, height);
        fillPath.lineTo(0, height);
        fillPath.close();
        canvas.drawPath(fillPath, fillPaint);
    }
}
