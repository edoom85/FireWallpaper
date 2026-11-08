package com.ignis.wallpaper;

import android.opengl.GLES20;

public class FireEmitter extends Emitter {

    public FireEmitter() {
        meshName = "fire1";
        spawnRate = 0.06f;
        spawnJitter = 0.0f;
        textureName = "fire1";
        colorStart = new float[]{1.0f, 1.0f, 1.0f, 1.0f};
        colorEnd   = new float[]{1.0f, 0.25f, 0.25f, 0.0f};
        posJitterX = 5.5f;
        posJitterY = 1.0f;
        blendSrc = GLES20.GL_SRC_ALPHA;
        blendDst = GLES20.GL_ONE;
    }

    public void update(float dt, float[] startColor, float[] destColor, float flameDensity, float flameNoise, String hotspot) {
        colorStart[0] = startColor[0];
        colorStart[1] = startColor[1];
        colorStart[2] = startColor[2];
        colorStart[3] = 1.0f;

        colorEnd[0] = destColor[0];
        colorEnd[1] = destColor[1];
        colorEnd[2] = destColor[2];
        colorEnd[3] = 0.0f;

        spawnRate = ((1.0f - flameDensity) * 0.09f) + 0.05f;
        textureName = ("bfire".equals(hotspot) || "gfire".equals(hotspot) || "wfire".equals(hotspot) || "hdfire".equals(hotspot)) ?
                (hotspot + "1") : "fire1";

        super.update(dt);
    }

    @Override
    public void setupParticle(Particle p) {
        super.setupParticle(p);
        p.lifetime = 1.25f;
        float s = rand(0.4f, 0.8f);
        p.startScale = new float[]{s, s, s};
        float sEnd = rand(0.2f, 0.4f);
        p.endScale = new float[]{sEnd, sEnd, sEnd};

        float a1 = rand(-5.0f, 5.0f) * 0.5f;
        float a2 = rand(-15.0f, 15.0f) * 0.5f;
        p.startVel = new float[]{a1, 0.0f, 14.0f};
        p.endVel   = new float[]{a2, 0.0f, 22.0f};
        p.startAngle = 0f;
        p.endAngle = 0f;
    }
}
