package com.imflawlezz.lab4;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    private DrawingSurfaceView drawingSurfaceView;
    private MaterialButton btnColorPicker;
    private boolean isMarkersEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);
        drawingSurfaceView = findViewById(R.id.canvas_frame);
        btnColorPicker = findViewById(R.id.btn_color_picker);

        setupTools();
        setupColors();
        setupMenu();
        setupColorPicker();
    }

    private void setupTools() {
        MaterialButton btnBrush = findViewById(R.id.btn_brush);
        MaterialButton btnFill = findViewById(R.id.btn_fill);
        MaterialButton btnEraser = findViewById(R.id.btn_eraser);
        MaterialButton btnClear = findViewById(R.id.btn_clear);

        btnBrush.setOnClickListener(v -> {
            drawingSurfaceView.setEraserMode(false);
            drawingSurfaceView.setFillMode(false);
        });

        btnFill.setOnClickListener(v -> {
            drawingSurfaceView.setEraserMode(false);
            drawingSurfaceView.setFillMode(true);
        });

        btnEraser.setOnClickListener(v -> {
            drawingSurfaceView.setEraserMode(true);
            drawingSurfaceView.setFillMode(false);
        });

        btnClear.setOnClickListener(v -> drawingSurfaceView.clearCanvas());
    }

    private void setupMenu() {
        MaterialButton btnMenu = findViewById(R.id.btn_menu);

        btnMenu.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(this, btnMenu);
            popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

            Menu menu = popupMenu.getMenu();
            menu.findItem(R.id.menu_markers).setChecked(isMarkersEnabled);

            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.menu_markers) {
                    isMarkersEnabled = !item.isChecked();
                    drawingSurfaceView.setMarkersEnabled(isMarkersEnabled);
                    item.setChecked(isMarkersEnabled);
                    return true;
                }
                return false;
            });

            popupMenu.show();
        });
    }


    private void setupColors() {
        findViewById(R.id.btn_red).setOnClickListener(v -> drawingSurfaceView.setBrushColor(Color.RED));
        findViewById(R.id.btn_yellow).setOnClickListener(v -> drawingSurfaceView.setBrushColor(Color.YELLOW));
        findViewById(R.id.btn_blue).setOnClickListener(v -> drawingSurfaceView.setBrushColor(Color.BLUE));
        findViewById(R.id.btn_green).setOnClickListener(v -> drawingSurfaceView.setBrushColor(Color.GREEN));
        findViewById(R.id.btn_purple).setOnClickListener(v -> drawingSurfaceView.setBrushColor(Color.MAGENTA));

    }

    private void setupColorPicker() {
        btnColorPicker.setOnClickListener(v -> {
            CustomColorPickerDialog colorPickerDialog = new CustomColorPickerDialog(this, drawingSurfaceView);
            colorPickerDialog.show();
        });
    }

}
