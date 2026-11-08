package com.ignis.wallpaper;

public class Particle {

    public float[] startVel   = new float[3];
    public float[] endVel     = new float[3];
    public float[] startScale = new float[]{1f, 1f, 1f};
    public float[] endScale   = new float[]{1f, 1f, 1f};
    public float startAngle   = 0f;
    public float endAngle     = 0f;
    public float angVel       = 0f;
    public float lifetime     = 1f;
    public boolean active     = false;

    public float[] curVel   = new float[3];
    public float[] curScale = new float[]{1f, 1f, 1f};
    public float[] curColor = new float[]{1f, 1f, 1f, 1f};
    public float[] pos      = new float[3];
    public float angle      = 0f;
    public float age        = 0f;

    private boolean hasAngleLerp = false;
    private boolean hasScaleLerp = false;

    public void reset() {
        pos[0] = 0f; pos[1] = 0f; pos[2] = 0f;
        age = 0f;
        startVel[0] = 0f; startVel[1] = 0f; startVel[2] = 0f;
        endVel[0] = 0f; endVel[1] = 0f; endVel[2] = 0f;
        startScale[0] = 1f; startScale[1] = 1f; startScale[2] = 1f;
        endScale[0] = 1f; endScale[1] = 1f; endScale[2] = 1f;
        startAngle = 0f;
        endAngle = 0f;
        angVel = 0f;
        lifetime = 1f;
    }

    public void init() {
        if (startAngle != endAngle) {
            hasAngleLerp = true;
        } else {
            hasAngleLerp = false;
            angle = startAngle;
        }

        if (startScale[0] == 1f && startScale[1] == 1f && startScale[2] == 1f &&
            endScale[0] == 1f && endScale[1] == 1f && endScale[2] == 1f) {
            hasScaleLerp = false;
        } else {
            hasScaleLerp = true;
        }
    }

    public boolean update(float dt, Emitter emitter) {
        age += dt;
        if (age > lifetime) {
            active = false;
            return false;
        }

        float f2 = age / lifetime;
        float f3 = 1.0f - f2;

        curVel[0] = startVel[0] * f3 + endVel[0] * f2;
        curVel[1] = startVel[1] * f3 + endVel[1] * f2;
        curVel[2] = startVel[2] * f3 + endVel[2] * f2;

        curColor[0] = emitter.colorStart[0] * f3 + emitter.colorEnd[0] * f2;
        curColor[1] = emitter.colorStart[1] * f3 + emitter.colorEnd[1] * f2;
        curColor[2] = emitter.colorStart[2] * f3 + emitter.colorEnd[2] * f2;
        curColor[3] = emitter.colorStart[3] * f3 + emitter.colorEnd[3] * f2;

        if (hasScaleLerp) {
            curScale[0] = startScale[0] * f3 + endScale[0] * f2;
            curScale[1] = startScale[1] * f3 + endScale[1] * f2;
            curScale[2] = startScale[2] * f3 + endScale[2] * f2;
        }

        if (hasAngleLerp) {
            angle = f2 * endAngle + f3 * startAngle;
        } else {
            angle += angVel * dt;
        }

        pos[0] += curVel[0] * dt;
        pos[1] += curVel[1] * dt;
        pos[2] += curVel[2] * dt;

        return true;
    }
}
