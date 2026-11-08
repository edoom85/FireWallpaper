package com.ignis.wallpaper;

import android.opengl.GLES20;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class Mesh {

    public final int vbo;
    public final int uvbo;
    public final int ibo;
    public final int count;

    public Mesh(float[] positions, float[] uvs, short[] indices) {
        this.count = indices.length;

        FloatBuffer posBuf = ByteBuffer.allocateDirect(positions.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer().put(positions);
        posBuf.position(0);

        FloatBuffer uvBuf = ByteBuffer.allocateDirect(uvs.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer().put(uvs);
        uvBuf.position(0);

        ShortBuffer idxBuf = ByteBuffer.allocateDirect(indices.length * 2)
                .order(ByteOrder.nativeOrder()).asShortBuffer().put(indices);
        idxBuf.position(0);

        int[] buffers = new int[3];
        GLES20.glGenBuffers(3, buffers, 0);
        vbo = buffers[0];
        uvbo = buffers[1];
        ibo = buffers[2];

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vbo);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, positions.length * 4, posBuf, GLES20.GL_STATIC_DRAW);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, uvbo);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, uvs.length * 4, uvBuf, GLES20.GL_STATIC_DRAW);

        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, ibo);
        GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, indices.length * 2, idxBuf, GLES20.GL_STATIC_DRAW);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    public static Mesh createPlane16x16() {
        float[] pos = {
            8.0f, 0.0f, 8.0f,
            -7.9998f, 0.0f, 8.0f,
            -8.0f, 0.0f, -7.9998f,
            8.0f, 0.0f, -8.0f
        };
        float[] uvs = {
            1.0f, 0.0f,
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f
        };
        short[] idx = { 0, 1, 3, 1, 2, 3 };
        return new Mesh(pos, uvs, idx);
    }

    public static Mesh createFire1() {
        float[] pos = {
            -3.9838f, 0.0f, -7.9998f,
            8.0f, 0.0f, 5.2666f,
            1.3522f, 0.0f, 8.0f,
            -8.0f, 0.0f, -4.4906f,
            8.0f, 0.0f, -3.5271f,
            4.5735f, 0.0f, -7.9998f,
            -3.3238f, 0.0f, 8.0f,
            -8.0336f, 0.0f, 4.7253f
        };
        float[] uvs = {
            0.2523f, 0.9993f,
            1.0f, 0.1708f,
            0.5846f, 0.0027f,
            -0.0012f, 0.8052f,
            1.0f, 0.7038f,
            0.775f, 0.9997f,
            0.2198f, 0.0039f,
            0.0019f, 0.2076f
        };
        short[] idx = {
            0, 1, 3,  3, 1, 2,
            2, 6, 3,  3, 6, 7,
            4, 1, 5,  1, 0, 5
        };
        return new Mesh(pos, uvs, idx);
    }

    public static Mesh createFire2() {
        float[] pos = {
            -3.2403f, 1.7906f, 5.0245f,
            -3.2403f, 1.7906f, 0.7077f,
            1.0762f, 1.7906f, 0.7077f,
            1.0762f, 1.7906f, 5.0245f,
            5.653f, -2.9159f, -3.6613f,
            5.653f, -2.9159f, 2.4677f,
            -0.476f, -2.9159f, 2.4677f,
            -0.476f, -2.9159f, -3.6613f,
            1.0951f, -1.0845f, -4.4251f,
            1.0951f, -1.0845f, -0.1083f,
            -3.2214f, -1.0845f, -0.1083f,
            -3.2214f, -1.0845f, -4.4251f,
            -9.1399f, 0.0f, 7.0124f,
            -5.6699f, 0.0f, 4.4451f,
            -3.1022f, 0.0f, 7.9155f,
            -6.5726f, 0.0f, 10.4828f,
            9.5526f, -2.4229f, -0.6356f,
            6.2667f, -2.4229f, -3.4353f,
            9.0664f, -2.4229f, -6.7212f,
            12.3523f, -2.4229f, -3.9215f,
            -4.4738f, 4.7114f, -2.1354f,
            -7.3225f, 4.7114f, -5.3791f,
            -4.0792f, 4.7114f, -8.2275f,
            -1.2305f, 4.7114f, -4.9842f,
            6.0298f, 0.0f, 0.5334f,
            9.5968f, 0.0f, 2.9645f,
            7.1656f, 0.0f, 6.5319f,
            3.5983f, 0.0f, 4.1007f
        };
        float[] uvs = {
            0.517f, 0.0137f,  0.517f, 0.4726f,  0.976f, 0.4726f,  0.976f, 0.0137f,
            0.9537f, 0.9856f, 0.9537f, 0.5314f, 0.4996f, 0.5314f, 0.4996f, 0.9856f,
            0.4658f, 0.9798f, 0.4658f, 0.5349f, 0.0209f, 0.5349f, 0.0209f, 0.9798f,
            0.976f, 0.4726f,  0.976f, 0.0137f,  0.517f, 0.0137f,  0.517f, 0.4726f,
            0.4658f, 0.9798f, 0.4658f, 0.5349f, 0.0209f, 0.5349f, 0.0209f, 0.9798f,
            0.4658f, 0.9798f, 0.4658f, 0.5349f, 0.0209f, 0.5349f, 0.0209f, 0.9798f,
            0.976f, 0.4726f,  0.976f, 0.0137f,  0.517f, 0.0137f,  0.517f, 0.4726f
        };
        short[] idx = {
            0, 1, 3,  3, 1, 2,
            4, 5, 7,  7, 5, 6,
            8, 9, 11, 11, 9, 10,
            12, 13, 15, 15, 13, 14,
            16, 17, 19, 19, 17, 18,
            20, 21, 23, 23, 21, 22,
            24, 25, 27, 25, 26, 27
        };
        return new Mesh(pos, uvs, idx);
    }
}
