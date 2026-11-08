package com.ignis.wallpaper;

import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private GLSurfaceView glSurfaceView;
    private FireRenderer renderer;
    private SharedPreferences prefs;
    private LinearLayout presetContainer;
    private View bottomPanel;
    private View topBar;
    private Button btnToggleUi;
    private boolean isUiVisible = true;

    private static class Preset {
        final String id;
        final String name;
        final String emoji;
        final String hotspot;
        final float[] startColor;
        final float[] destColor;
        final int color;

        Preset(String id, String name, String emoji, String hotspot, float[] startColor, float[] destColor, int color) {
            this.id = id;
            this.name = name;
            this.emoji = emoji;
            this.hotspot = hotspot;
            this.startColor = startColor;
            this.destColor = destColor;
            this.color = color;
        }
    }

    private final List<Preset> presets = new ArrayList<>();
    private final List<Button> presetButtons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences("WallpaperPrefs", MODE_PRIVATE);

        initPresets();

        glSurfaceView = findViewById(R.id.glSurfaceView);
        glSurfaceView.setEGLContextClientVersion(2);

        renderer = new FireRenderer(this);
        loadCurrentPreferences();
        glSurfaceView.setRenderer(renderer);

        topBar = findViewById(R.id.topBar);
        bottomPanel = findViewById(R.id.bottomPanel);
        btnToggleUi = findViewById(R.id.btnToggleUi);
        presetContainer = findViewById(R.id.presetContainer);

        buildPresetButtons();

        glSurfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
                    float x = event.getX() / v.getWidth();
                    float y = event.getY() / v.getHeight();
                    renderer.onTouch(x, y);
                    renderer.onOffset(x);
                    return true;
                }
                return false;
            }
        });

        btnToggleUi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isUiVisible = !isUiVisible;
                bottomPanel.setVisibility(isUiVisible ? View.VISIBLE : View.GONE);
                btnToggleUi.setText(isUiVisible ? "👁️ Ocultar" : "👁️ Mostrar");
            }
        });

        Button btnSetWallpaper = findViewById(R.id.btnSetWallpaper);
        btnSetWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
                intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                        new ComponentName(MainActivity.this, FireWallpaperService.class));
                startActivity(intent);
            }
        });
    }

    private void initPresets() {
        presets.add(new Preset("classic", "Classic", "🔥", "fire",
                new float[]{1.0f, 0.75f, 0.3f, 1.0f},
                new float[]{1.0f, 0.27f, 0.27f, 1.0f},
                Color.parseColor("#FF8C00")));

        presets.add(new Preset("hdfire", "HD Fire", "✨", "hdfire",
                new float[]{1.0f, 0.75f, 0.3f, 1.0f},
                new float[]{1.0f, 0.27f, 0.27f, 1.0f},
                Color.parseColor("#FFA726")));

        presets.add(new Preset("blue", "Blue Ice", "❄️", "bfire",
                new float[]{0.3f, 0.75f, 1.0f, 1.0f},
                new float[]{0.1f, 0.3f, 0.9f, 1.0f},
                Color.parseColor("#00BFFF")));

        presets.add(new Preset("emerald", "Emerald", "🌲", "gfire",
                new float[]{0.3f, 1.0f, 0.4f, 1.0f},
                new float[]{0.05f, 0.6f, 0.1f, 1.0f},
                Color.parseColor("#00E676")));

        presets.add(new Preset("purple", "Purple", "💜", "fire",
                new float[]{0.9f, 0.3f, 1.0f, 1.0f},
                new float[]{0.5f, 0.05f, 0.7f, 1.0f},
                Color.parseColor("#E040FB")));

        presets.add(new Preset("cyan", "Cyan Neon", "⚡", "wfire",
                new float[]{0.3f, 0.9f, 1.0f, 1.0f},
                new float[]{0.1f, 0.5f, 0.8f, 1.0f},
                Color.parseColor("#00E5FF")));

        presets.add(new Preset("toxic", "Toxic", "☣️", "gfire",
                new float[]{0.8f, 1.0f, 0.2f, 1.0f},
                new float[]{0.2f, 0.6f, 0.0f, 1.0f},
                Color.parseColor("#AEEA00")));
    }

    private void buildPresetButtons() {
        presetContainer.removeAllViews();
        presetButtons.clear();

        String currentHotspot = prefs.getString("pref_hotspot", "fire");

        for (int i = 0; i < presets.size(); i++) {
            final Preset p = presets.get(i);
            final Button btn = new Button(this);
            btn.setText(p.emoji + " " + p.name);
            btn.setTextSize(12);
            btn.setTextColor(Color.WHITE);
            btn.setPadding(32, 16, 32, 16);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 0, 16, 0);
            btn.setLayoutParams(lp);

            updateButtonBackground(btn, p.hotspot.equals(currentHotspot));

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    applyPreset(p);
                    for (int j = 0; j < presetButtons.size(); j++) {
                        updateButtonBackground(presetButtons.get(j), presets.get(j) == p);
                    }
                }
            });

            presetButtons.add(btn);
            presetContainer.addView(btn);
        }
    }

    private void updateButtonBackground(Button btn, boolean selected) {
        GradientDrawable gd = new GradientDrawable();
        gd.setCornerRadius(24);
        if (selected) {
            gd.setColor(Color.parseColor("#2E1A00"));
            gd.setStroke(4, Color.parseColor("#FF6A00"));
            btn.setTextColor(Color.parseColor("#FFA726"));
        } else {
            gd.setColor(Color.parseColor("#1E1E1E"));
            gd.setStroke(2, Color.parseColor("#333333"));
            btn.setTextColor(Color.parseColor("#DDDDDD"));
        }
        btn.setBackground(gd);
    }

    private void applyPreset(Preset p) {
        renderer.hotspot = p.hotspot;
        System.arraycopy(p.startColor, 0, renderer.startColor, 0, 4);
        System.arraycopy(p.destColor, 0, renderer.destColor, 0, 4);

        SharedPreferences.Editor ed = prefs.edit();
        ed.putString("pref_hotspot", p.hotspot);
        ed.putString("pref_startcolor", p.startColor[0] + " " + p.startColor[1] + " " + p.startColor[2] + " " + p.startColor[3]);
        ed.putString("pref_destcolor", p.destColor[0] + " " + p.destColor[1] + " " + p.destColor[2] + " " + p.destColor[3]);
        ed.apply();
    }

    private void loadCurrentPreferences() {
        renderer.hotspot = prefs.getString("pref_hotspot", "fire");
        parseColor(prefs.getString("pref_startcolor", "1.0 0.75 0.3 1.0"), renderer.startColor);
        parseColor(prefs.getString("pref_destcolor", "1.0 0.27 0.27 1.0"), renderer.destColor);
    }

    private void parseColor(String str, float[] out) {
        if (str == null) return;
        String[] parts = str.trim().split("\\s+");
        if (parts.length >= 3) {
            try {
                out[0] = Float.parseFloat(parts[0]);
                out[1] = Float.parseFloat(parts[1]);
                out[2] = Float.parseFloat(parts[2]);
                if (parts.length >= 4) out[3] = Float.parseFloat(parts[3]);
            } catch (Exception ignored) {}
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (glSurfaceView != null) {
            glSurfaceView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (glSurfaceView != null) {
            glSurfaceView.onPause();
        }
    }
}
