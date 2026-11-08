package com.ignis.wallpaper;

import android.opengl.GLES20;

public class BurstEmitter extends Emitter {

    public final float[] customPos = new float[]{0f, 20f, -13f};

    public BurstEmitter() {
        meshName = "plane_16x16";
        spawnRate = 0.01f;
        spawnJitter = 0.0f;
        burstCount = 6;
        textureName = "fire3";
        colorStart = new float[]{1.0f, 1.0f, 1.0f, 1.0f};
        colorEnd   = new float[]{1.0f, 0.5f, 0.5f, 0.0f};
        posJitterX = 1.0f;
        posJitterZ = 0.25f;
        active = false;
        blendSrc = GLES20.GL_SRC_ALPHA;
        blendDst = GLES20.GL_ONE;
    }

    public void trigger(float x, float y, float z) {
        customPos[0] = x;
        customPos[1] = y;
        customPos[2] = z;
        burstCount = 6;
        active = true;
    }

    public void update(float dt, float[] startColor, float[] destColor, String hotspot) {
        colorStart[0] = startColor[0];
        colorStart[1] = startColor[1];
        colorStart[2] = startColor[2];
        colorEnd[0] = destColor[0];
        colorEnd[1] = destColor[1];
        colorEnd[2] = destColor[2];

        textureName = ("bfire".equals(hotspot) || "gfire".equals(hotspot) || "wfire".equals(hotspot) || "hdfire".equals(hotspot)) ?
                (hotspot + "3") : "fire3";

        super.update(dt);
    }

    @Override
    public void setupParticle(Particle p) {
        super.setupParticle(p);
        p.lifetime = 1.0f;
        float s = rand(0.1f, 0.2f);
        p.startScale = new float[]{s, s, s};
        float sEnd = rand(0.2f, 0.4f);
        p.endScale = new float[]{sEnd, sEnd, sEnd};

        float a  = rand(-3.0f, 3.0f);
        float a2 = rand(-3.0f, 3.0f);
        float a3 = rand(-3.0f, 3.0f);
        p.startVel = new float[]{a, a2, a3};
        p.endVel   = new float[]{a * 0.5f, a2 * 0.5f, a3 * 0.5f};
        p.startAngle = rand(0.0f, 360.0f);
        p.endAngle   = p.startAngle + 45.0f;
    }
}
