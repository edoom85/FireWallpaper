package com.ignis.wallpaper;

import android.opengl.GLES20;
import java.util.Random;

public class Emitter {

    protected final Random random = new Random();

    public float[] colorStart = new float[]{1f, 1f, 1f, 1f};
    public float[] colorEnd   = new float[]{1f, 1f, 1f, 1f};
    public String meshName    = "plane_16x16";
    public String textureName = "fire1";
    public float spawnRate    = 1.0f;
    public float spawnJitter  = 0.2f;
    public int burstCount     = 0;
    public float posJitterX   = 0f;
    public float posJitterY   = 0f;
    public float posJitterZ   = 0f;
    public boolean active     = true;
    public int blendSrc       = GLES20.GL_SRC_ALPHA;
    public int blendDst       = GLES20.GL_ONE;

    private float timerX = 0f;
    private float timerY = 0f;
    public int activeCount = 0;
    public final int maxParticles = 32;
    public final Particle[] particles = new Particle[32];

    public Emitter() {
        for (int i = 0; i < maxParticles; i++) {
            particles[i] = new Particle();
        }
    }

    protected float rand(float min, float max) {
        return min + random.nextFloat() * (max - min);
    }

    public void setupParticle(Particle p) {
        p.reset();
        p.pos[0] = posJitterX > 0.01f ? rand(-posJitterX, posJitterX) : 0f;
        p.pos[1] = posJitterY > 0.01f ? rand(-posJitterY, posJitterY) : 0f;
        p.pos[2] = posJitterZ > 0.01f ? rand(-posJitterZ, posJitterZ) : 0f;
        p.active = true;
    }

    public void update(float dt) {
        int toSpawn = 0;
        if (active) {
            if (burstCount > 0) {
                toSpawn = burstCount;
                active = false;
            } else if (activeCount < maxParticles) {
                timerX += dt;
                while ((timerX + timerY) >= spawnRate) {
                    timerX -= (spawnRate + timerY);
                    timerY = spawnJitter > 0f ? rand(-spawnJitter, spawnJitter) : 0f;
                    toSpawn++;
                }
            }
        }

        for (int i = 0; i < maxParticles; i++) {
            Particle p = particles[i];
            if (p.active) {
                if (!p.update(dt, this)) {
                    activeCount--;
                }
            } else if (toSpawn > 0) {
                float initialAge = 0.001f;
                if (toSpawn > 1 && burstCount == 0) {
                    initialAge = (toSpawn - 1) * spawnRate;
                }
                setupParticle(p);
                p.init();
                p.update(initialAge, this);
                activeCount++;
                toSpawn--;
            }
        }
    }
}
