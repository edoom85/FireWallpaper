package com.ignis.wallpaper;

import android.opengl.GLES20;

public class CoreGlowEmitter extends Emitter {

    public CoreGlowEmitter() {
        spawnRate = 0.1f;
        spawnJitter = 0.0f;
        meshName = "plane_16x16";
        textureName = "fire4";
        colorStart = new float[]{1.0f, 1.0f, 1.0f, 3.0f};
        colorEnd   = new float[]{1.0f, 1.0f, 1.0f, 0.0f};
        posJitterX = 12.0f;
        posJitterY = 0.5f;
        blendSrc = GLES20.GL_SRC_ALPHA;
        blendDst = GLES20.GL_ONE;
    }

    public void update(float dt, float[] startColor, float[] destColor, float flameNoise, String hotspot) {
        colorStart[0] = startColor[0] * startColor[0];
        colorStart[1] = startColor[1] * startColor[1];
        colorStart[2] = startColor[2] * startColor[2];
        colorStart[3] = 3.0f;

        colorEnd[0] = destColor[0];
        colorEnd[1] = destColor[1];
        colorEnd[2] = destColor[2];
        colorEnd[3] = 0.0f;

        textureName = ("bfire".equals(hotspot) || "gfire".equals(hotspot) || "wfire".equals(hotspot) || "hdfire".equals(hotspot)) ?
                (hotspot + "4") : "fire4";

        super.update(dt);
    }

    @Override
    public void setupParticle(Particle p) {
        super.setupParticle(p);
        p.lifetime = 3.0f;
        float s = rand(0.01f, 0.05f);
        p.startScale = new float[]{s, s, s};
        float sEnd = rand(0.01f, 0.05f);
        p.endScale = new float[]{sEnd, sEnd, sEnd};

        float a1 = rand(-5.0f, 5.0f) * 0.5f;
        float a2 = rand(-10.0f, 10.0f) * 0.5f;
        p.startVel = new float[]{a1, 0.0f, 6.0f};
        p.endVel   = new float[]{a2, 0.0f, 10.0f};
        p.startAngle = rand(-360.0f, 360.0f);
        p.endAngle   = rand(-360.0f, 360.0f);
    }
}
