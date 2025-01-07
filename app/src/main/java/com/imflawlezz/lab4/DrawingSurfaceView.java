package com.imflawlezz.lab4;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import java.util.LinkedList;
import java.util.Queue;

public class DrawingSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private Bitmap bitmap;
    private Canvas bitmapCanvas;
    private Paint paint;
    private Paint eraserPaint;
    private float startX, startY;
    private boolean isEraserMode = false;
    private boolean isFillMode = false;
    private boolean markersEnabled = false;

    public DrawingSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.d("DrawingSurfaceView", "SurfaceView created");
        init();
    }

    private void init() {
        getHolder().addCallback(this);

        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10f);
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);

        eraserPaint = new Paint();
        eraserPaint.setStyle(Paint.Style.STROKE);
        eraserPaint.setStrokeWidth(100f);
        eraserPaint.setAntiAlias(true);
        eraserPaint.setStrokeCap(Paint.Cap.ROUND);
        eraserPaint.setColor(Color.WHITE);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        bitmapCanvas = new Canvas(bitmap);
        bitmapCanvas.drawColor(Color.WHITE);
        drawCanvas();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
        if (bitmap != null) {
            Bitmap newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas newCanvas = new Canvas(newBitmap);
            newCanvas.drawBitmap(bitmap, 0, 0, null);
            bitmap = newBitmap;
            bitmapCanvas = newCanvas;
        }
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        bitmap.recycle();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isFillMode) {
                    floodFillAsync((int) x, (int) y, bitmap.getPixel((int) x, (int) y), paint.getColor());
                } else {
                    startX = x;
                    startY = y;
                    if (markersEnabled && !isEraserMode) {
                        bitmapCanvas.drawCircle(startX, startY, 10, paint);
                    }
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (!isFillMode && !isEraserMode) {
                    bitmapCanvas.drawLine(startX, startY, x, y, paint);
                    startX = x;
                    startY = y;
                } else if (isEraserMode) {
                    bitmapCanvas.drawLine(startX, startY, x, y, eraserPaint);
                    startX = x;
                    startY = y;
                }
                break;

            case MotionEvent.ACTION_UP:
                if (!isFillMode && !isEraserMode && markersEnabled) {
                    bitmapCanvas.drawCircle(startX, startY, 10, paint);
                }
                break;
        }
        drawCanvas();
        return true;
    }

    private void floodFill(int x, int y, int targetColor, int replacementColor) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        if (targetColor == replacementColor) return;

        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        int startIndex = y * width + x;
        if (pixels[startIndex] != targetColor) return;

        Queue<Point> queue = new LinkedList<>();
        queue.add(new Point(x, y));

        while (!queue.isEmpty()) {
            Point p = queue.poll();
            int index = p.y * width + p.x;

            if (p.x < 0 || p.x >= width || p.y < 0 || p.y >= height || pixels[index] != targetColor) continue;

            pixels[index] = replacementColor;

            queue.add(new Point(p.x + 1, p.y));
            queue.add(new Point(p.x - 1, p.y));
            queue.add(new Point(p.x, p.y + 1));
            queue.add(new Point(p.x, p.y - 1));
        }

        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);

        drawCanvas();
    }

    private void floodFillAsync(int x, int y, int targetColor, int replacementColor) {
        new Thread(() -> floodFill(x, y, targetColor, replacementColor)).start();
    }


    private void drawCanvas() {
        Canvas canvas = getHolder().lockCanvas();
        if (canvas != null) {
            canvas.drawBitmap(bitmap, 0, 0, null);
            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    public void setBrushColor(int color) {
        paint.setColor(color);
        isEraserMode = false;
    }

    public void setEraserMode(boolean eraserMode) {
        isEraserMode = eraserMode;
    }

    public void clearCanvas() {
        bitmapCanvas.drawColor(Color.WHITE);
        drawCanvas();
    }

    public void setMarkersEnabled(boolean enabled) {
        markersEnabled = enabled;
    }

    public void setFillMode(boolean enabled) {
        this.isFillMode = enabled;
    }
}
