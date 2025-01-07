package com.imflawlezz.lab4;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.annotation.NonNull;

public class CustomColorPickerDialog extends Dialog {

    private final DrawingSurfaceView drawingSurfaceView;
    private SeekBar seekBarR, seekBarG, seekBarB;
    private ImageView colorPreview;

    public CustomColorPickerDialog(@NonNull Context context, DrawingSurfaceView drawingSurfaceView) {
        super(context);
        this.drawingSurfaceView = drawingSurfaceView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_color_picker);
        int width = (int) (getContext().getResources().getDisplayMetrics().widthPixels * 0.8);
        int height = (int) (getContext().getResources().getDisplayMetrics().heightPixels * 0.8);

        if (getWindow() != null) {
            getWindow().setLayout(width, height);
        }

        seekBarR = findViewById(R.id.seekBarR);
        seekBarG = findViewById(R.id.seekBarG);
        seekBarB = findViewById(R.id.seekBarB);
        colorPreview = findViewById(R.id.colorPreview);
        Button selectColorButton = findViewById(R.id.selectColorButton);

        seekBarR.setProgress(255);
        seekBarG.setProgress(0);
        seekBarB.setProgress(0);
        updateColorPreview();

        seekBarR.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateColorPreview();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        seekBarG.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateColorPreview();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        seekBarB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateColorPreview();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        selectColorButton.setOnClickListener(v -> {
            int color = Color.rgb(seekBarR.getProgress(), seekBarG.getProgress(), seekBarB.getProgress());
            drawingSurfaceView.setBrushColor(color);
            dismiss();
        });
    }

    private void updateColorPreview() {
        if (seekBarR == null || seekBarG == null || seekBarB == null) {
            return;
        }
        int color = Color.rgb(seekBarR.getProgress(), seekBarG.getProgress(), seekBarB.getProgress());
        colorPreview.setBackgroundColor(color);
    }
}
