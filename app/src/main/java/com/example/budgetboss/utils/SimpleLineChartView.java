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

        float paddingVertical = 40f;
        float usableHeight = height - (2 * paddingVertical);

        // Normalize data to fit height
        float max = 0;
        float min = Float.MAX_VALUE;
        for (float f : dataPoints) {
            if (f > max)
                max = f;
            if (f < min)
                min = f;
        }

        // If all values are same or 0
        if (max == min) {
            max = max + 1; // Avoid 0 devision
        }
        float range = max - min;
        if (range == 0)
            range = 1;

        if (dataPoints.size() == 1) {
            // Draw single point line
            canvas.drawLine(0, height / 2, width, height / 2, linePaint);
            return;
        }

        float spacing = width / (dataPoints.size() - 1);

        linePath.reset();
        // Calculate Y: height - paddingBottom - (normalized * usableHeight)
        // normalized: (val - min) / range;
        // Let's perform simple relative to 0 for budget charts usually
        // Or relative to min? Let's stick to relative to 0 for budget (absolute amount)
        // If we want relative to 0, range is just max.

        float startY = height - paddingVertical - (dataPoints.get(0) / max * usableHeight);
        linePath.moveTo(0, startY);

        for (int i = 1; i < dataPoints.size(); i++) {
            float x = i * spacing;
            float y = height - paddingVertical - (dataPoints.get(i) / max * usableHeight);

            // Create cubic bezier for smooth curve
            float prevX = (i - 1) * spacing;
            float prevY = height - paddingVertical - (dataPoints.get(i - 1) / max * usableHeight);
            float controlX1 = prevX + (x - prevX) / 2;
            float controlX2 = x - (x - prevX) / 2;

            linePath.cubicTo(controlX1, prevY, controlX2, y, x, y);
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
