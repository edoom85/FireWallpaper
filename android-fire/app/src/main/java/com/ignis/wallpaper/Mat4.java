package com.ignis.wallpaper;

public final class Mat4 {

    public static float[] create() {
        return new float[16];
    }

    public static void perspective(float[] out, float fovYDeg, float aspect, float near, float far) {
        float tan = near * (float) Math.tan(0.008726646f * fovYDeg);
        float right = tan * aspect;
        float top = tan;

        for (int i = 0; i < 16; i++) out[i] = 0f;
        out[0] = near / right;
        out[5] = near / top;
        out[10] = (far + near) / (near - far);
        out[11] = -1.0f;
        out[14] = (2.0f * far * near) / (near - far);
    }

    public static void lookAt(float[] out, float eyeX, float eyeY, float eyeZ,
                              float targetX, float targetY, float targetZ,
                              float upX, float upY, float upZ) {
        float f11 = targetX - eyeX;
        float f12 = targetY - eyeY;
        float f13 = targetZ - eyeZ;
        float len = 1.0f / (float) Math.hypot(f11, Math.hypot(f12, f13));
        float f14 = f11 * len;
        float f15 = f12 * len;
        float f16 = f13 * len;

        float f17 = (f15 * upZ) - (f16 * upY);
        float f18 = (f16 * upX) - (f14 * upZ);
        float f19 = (f14 * upY) - (f15 * upX);
        float len2 = 1.0f / (float) Math.hypot(f17, Math.hypot(f18, f19));
        float f20 = f17 * len2;
        float f21 = f18 * len2;
        float f22 = f19 * len2;

        out[0] = f20;
        out[1] = (f21 * f16) - (f22 * f15);
        out[2] = -f14;
        out[3] = 0.0f;

        out[4] = f21;
        out[5] = (f22 * f14) - (f20 * f16);
        out[6] = -f15;
        out[7] = 0.0f;

        out[8] = f22;
        out[9] = (f20 * f15) - (f21 * f14);
        out[10] = -f16;
        out[11] = 0.0f;

        out[12] = -(out[0] * eyeX + out[4] * eyeY + out[8] * eyeZ);
        out[13] = -(out[1] * eyeX + out[5] * eyeY + out[9] * eyeZ);
        out[14] = -(out[2] * eyeX + out[6] * eyeY + out[10] * eyeZ);
        out[15] = 1.0f;
    }

    public static void multiply(float[] out, float[] a, float[] b) {
        for (int i = 0; i < 4; i++) {
            float ai0 = a[i], ai1 = a[i + 4], ai2 = a[i + 8], ai3 = a[i + 12];
            out[i]      = ai0 * b[0]  + ai1 * b[1]  + ai2 * b[2]  + ai3 * b[3];
            out[i + 4]  = ai0 * b[4]  + ai1 * b[5]  + ai2 * b[6]  + ai3 * b[7];
            out[i + 8]  = ai0 * b[8]  + ai1 * b[9]  + ai2 * b[10] + ai3 * b[11];
            out[i + 12] = ai0 * b[12] + ai1 * b[13] + ai2 * b[14] + ai3 * b[15];
        }
    }

    public static void translate(float[] out, float[] m, float tx, float ty, float tz) {
        System.arraycopy(m, 0, out, 0, 12);
        out[12] = m[0] * tx + m[4] * ty + m[8] * tz + m[12];
        out[13] = m[1] * tx + m[5] * ty + m[9] * tz + m[13];
        out[14] = m[2] * tx + m[6] * ty + m[10] * tz + m[14];
        out[15] = m[3] * tx + m[7] * ty + m[11] * tz + m[15];
    }
}
