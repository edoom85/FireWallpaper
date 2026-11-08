package com.ignis.wallpaper;

import android.opengl.GLES20;

public class SparksEmitter extends Emitter {

    public SparksEmitter() {
        meshName = "fire2";
        spawnRate = 0.06f;
        spawnJitter = 0.0f;
        textureName = "fire2";
        colorStart = new float[]{1.66f, 1.66f, 1.66f, 1.0f};
        colorEnd   = new float[]{0.0f, 0.0f, 0.0f, 0.0f};
        posJitterX = 12.0f;
        posJitterY = 1.0f;
        blendSrc = GLES20.GL_DST_COLOR; // 774
        blendDst = GLES20.GL_ONE;       // 1
    }

    public void update(float dt, float flameDensity, float flameNoise, String hotspot) {
        spawnRate = ((1.0f - flameDensity) * 0.08f) + 0.04f;
        textureName = "hdfire".equals(hotspot) ? "hdfire2" : "fire2";
        super.update(dt);
    }

    @Override
    public void setupParticle(Particle p) {
        super.setupParticle(p);
        p.lifetime = 1.5f;
        float s = rand(0.12f, 0.9f);
        p.startScale = new float[]{s, s, s};
        float sEnd = rand(0.25f, 0.5f);
        p.endScale = new float[]{sEnd, sEnd, sEnd};

        float a1 = rand(-5.0f, 5.0f) * 0.5f;
        float a2 = rand(-15.0f, 15.0f) * 0.5f;
        p.startVel = new float[]{a1, 0.0f, 10.0f};
        p.endVel   = new float[]{a2, 0.0f, 18.0f};
        p.startAngle = rand(-180.0f, 180.0f);
        p.endAngle   = p.startAngle + rand(-90.0f, 90.0f);
    }
}
