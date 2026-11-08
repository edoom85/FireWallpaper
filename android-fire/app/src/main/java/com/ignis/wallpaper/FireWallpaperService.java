package com.ignis.wallpaper;

import android.content.SharedPreferences;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

public class FireWallpaperService extends GLWallpaperService {

    @Override
    public Engine onCreateEngine() {
        return new FireEngine();
    }

    private class FireEngine extends GLEngine implements SharedPreferences.OnSharedPreferenceChangeListener {

        private FireRenderer renderer;
        private SharedPreferences prefs;

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            setEGLContextClientVersion(2);
            setPreserveEGLContextOnPause(true);

            renderer = new FireRenderer(FireWallpaperService.this);
            setRenderer(renderer);
            setTouchEventsEnabled(true);

            prefs = getSharedPreferences("WallpaperPrefs", MODE_PRIVATE);
            loadPreferences(prefs);
            prefs.registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onDestroy() {
            if (prefs != null) {
                prefs.unregisterOnSharedPreferenceChangeListener(this);
            }
            super.onDestroy();
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            loadPreferences(sharedPreferences);
        }

        private void loadPreferences(SharedPreferences p) {
            if (renderer == null || p == null) return;
            renderer.hotspot = p.getString("pref_hotspot", "fire");
            parseColor(p.getString("pref_startcolor", "1.0 0.75 0.3 1.0"), renderer.startColor);
            parseColor(p.getString("pref_destcolor", "1.0 0.27 0.27 1.0"), renderer.destColor);
            try {
                renderer.flameDensity = Float.parseFloat(p.getString("pref_flamedensity", "5")) * 0.1f;
                renderer.flameNoise   = Float.parseFloat(p.getString("pref_flamenoise", "5")) * 0.1f;
            } catch (Exception ignored) {}
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
        public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {
            super.onOffsetsChanged(xOffset, yOffset, xOffsetStep, yOffsetStep, xPixelOffset, yPixelOffset);
            if (renderer != null) {
                renderer.onOffset(xOffset);
            }
        }

        @Override
        public void onTouchEvent(MotionEvent event) {
            super.onTouchEvent(event);
            if (renderer != null && event.getAction() == MotionEvent.ACTION_DOWN) {
                SurfaceHolder holder = getSurfaceHolder();
                if (holder != null && holder.getSurfaceFrame() != null) {
                    float w = holder.getSurfaceFrame().width();
                    float h = holder.getSurfaceFrame().height();
                    if (w > 0 && h > 0) {
                        renderer.onTouch(event.getX() / w, event.getY() / h);
                    }
                }
            }
        }
    }
}
