package com.ignis.wallpaper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class FireRenderer implements GLSurfaceView.Renderer {

    private final Context context;

    private static final String VS_SOURCE =
        "attribute vec4 a_position;\n" +
        "attribute vec2 a_texcoord_0;\n" +
        "varying vec2 v_texcoord_0;\n" +
        "uniform mat4 u_mtx_modelviewproj;\n" +
        "uniform vec3 u_vec3_translation;\n" +
        "uniform vec3 u_vec3_scale;\n" +
        "uniform float u_float_angle;\n" +
        "void main() {\n" +
        "    float r = radians(u_float_angle);\n" +
        "    float rCos = cos(r);\n" +
        "    float rSin = sin(r);\n" +
        "    vec4 new_position;\n" +
        "    new_position.xyz = a_position.xyz * u_vec3_scale;\n" +
        "    new_position.w = a_position.w;\n" +
        "    float axisX = new_position.x * rCos - new_position.z * rSin;\n" +
        "    float axisY = new_position.y;\n" +
        "    float axisZ = new_position.x * rSin + new_position.z * rCos;\n" +
        "    new_position.x = axisX;\n" +
        "    new_position.y = axisY;\n" +
        "    new_position.z = axisZ;\n" +
        "    new_position.xyz += u_vec3_translation;\n" +
        "    gl_Position = u_mtx_modelviewproj * new_position;\n" +
        "    v_texcoord_0 = a_texcoord_0;\n" +
        "}\n";

    private static final String FS_SOURCE =
        "precision mediump float;\n" +
        "varying vec2 v_texcoord_0;\n" +
        "uniform sampler2D s_diffuse;\n" +
        "uniform vec4 u_color_0;\n" +
        "void main() {\n" +
        "    gl_FragColor = texture2D(s_diffuse, v_texcoord_0) * u_color_0;\n" +
        "}\n";

    private int program;
    private int a_position;
    private int a_texcoord_0;
    private int u_mtx_modelviewproj;
    private int u_vec3_translation;
    private int u_vec3_scale;
    private int u_float_angle;
    private int u_color_0;
    private int u_s_diffuse;

    private final Map<String, Mesh> meshes = new HashMap<>();
    private final Map<String, Integer> textures = new HashMap<>();

    public final CoreGlowEmitter glowEmitter = new CoreGlowEmitter();
    public final FireEmitter fireEmitter = new FireEmitter();
    public final SparksEmitter sparksEmitter = new SparksEmitter();
    public final BurstEmitter[] burstEmitters = new BurstEmitter[6];
    private int burstIndex = 0;

    public float[] startColor = new float[]{1.0f, 0.75f, 0.3f, 1.0f};
    public float[] destColor  = new float[]{1.0f, 0.27f, 0.27f, 1.0f};
    public String hotspot     = "fire";
    public float flameDensity = 0.5f;
    public float flameNoise   = 0.5f;

    private final float[] POS_LL = new float[]{-14.0f, 20.0f, -13.0f};
    private final float[] POS_L  = new float[]{ -7.0f, 20.0f, -13.0f};
    private final float[] POS_C  = new float[]{  0.0f, 20.0f, -13.0f};
    private final float[] POS_R  = new float[]{  7.0f, 20.0f, -13.0f};
    private final float[] POS_RR = new float[]{ 14.0f, 20.0f, -13.0f};

    private final float[] POS_GLOW_L = new float[]{-6.0f, 20.0f, -13.0f};
    private final float[] POS_GLOW_C = new float[]{ 0.0f, 20.0f, -13.0f};
    private final float[] POS_GLOW_R = new float[]{ 6.0f, 20.0f, -13.0f};

    private final float[] POS_SPARKS_L = new float[]{-5.0f, 20.0f, -14.0f};
    private final float[] POS_SPARKS_C = new float[]{ 0.0f, 20.0f, -14.0f};
    private final float[] POS_SPARKS_R = new float[]{ 5.0f, 20.0f, -14.0f};

    private final float[] projMatrix     = Mat4.create();
    private final float[] viewMatrix     = Mat4.create();
    private final float[] viewProjMatrix = Mat4.create();
    private final float[] mvpMatrix      = Mat4.create();

    private final float[] camPos = new float[]{0f, 0f, 0f};
    private final float[] targetCamPos = new float[]{0f, 0f, 0f};
    public float swipeN = 0.5f;
    private int screenWidth = 1080;
    private int screenHeight = 1920;
    private long lastTimeNanos = 0;

    public FireRenderer(Context context) {
        this.context = context;
        for (int i = 0; i < 6; i++) {
            burstEmitters[i] = new BurstEmitter();
        }
    }

    public void onTouch(float xRatio, float yRatio) {
        float worldX = (xRatio - 0.5f) * 24.0f;
        float worldZ = -13.0f + (1.0f - yRatio) * 20.0f;
        burstIndex = (burstIndex + 1) % burstEmitters.length;
        burstEmitters[burstIndex].trigger(worldX, 20.0f, worldZ);
    }

    public void onOffset(float xOffset) {
        this.swipeN = Math.max(0.0f, Math.min(1.0f, xOffset));
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        int vs = compileShader(GLES20.GL_VERTEX_SHADER, VS_SOURCE);
        int fs = compileShader(GLES20.GL_FRAGMENT_SHADER, FS_SOURCE);
        program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, vs);
        GLES20.glAttachShader(program, fs);
        GLES20.glLinkProgram(program);

        a_position          = GLES20.glGetAttribLocation(program, "a_position");
        a_texcoord_0        = GLES20.glGetAttribLocation(program, "a_texcoord_0");
        u_mtx_modelviewproj = GLES20.glGetUniformLocation(program, "u_mtx_modelviewproj");
        u_vec3_translation  = GLES20.glGetUniformLocation(program, "u_vec3_translation");
        u_vec3_scale        = GLES20.glGetUniformLocation(program, "u_vec3_scale");
        u_float_angle       = GLES20.glGetUniformLocation(program, "u_float_angle");
        u_color_0           = GLES20.glGetUniformLocation(program, "u_color_0");
        u_s_diffuse         = GLES20.glGetUniformLocation(program, "s_diffuse");

        meshes.put("plane_16x16", Mesh.createPlane16x16());
        meshes.put("fire1",       Mesh.createFire1());
        meshes.put("fire2",       Mesh.createFire2());

        String[] texNames = {
            "fire1", "fire2", "fire3", "fire4",
            "hdfire1", "hdfire2", "hdfire3", "hdfire4",
            "bfire1", "bfire3", "bfire4",
            "gfire1", "gfire3", "gfire4",
            "wfire1", "wfire3", "wfire4"
        };
        for (String t : texNames) {
            textures.put(t, loadTextureFromAssets("textures/" + t + "_ktx.png"));
        }

        lastTimeNanos = System.nanoTime();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        this.screenWidth = width;
        this.screenHeight = height;
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        long nowNanos = System.nanoTime();
        float dt = (nowNanos - lastTimeNanos) / 1_000_000_000.0f;
        if (dt > 0.1f) dt = 0.1f;
        lastTimeNanos = nowNanos;

        // Camera Update — gentle parallax that keeps flames filling the screen on 2-screen setups
        boolean isLandscape = screenWidth > screenHeight;
        float fov = isLandscape ? 35.0f : 60.0f;
        float aspect = (float) screenWidth / (float) screenHeight;

        targetCamPos[0] = (8.0f * swipeN) - 4.0f;
        float f2 = 5.0f * dt;
        camPos[0] += (targetCamPos[0] - camPos[0]) * f2;

        float f6 = camPos[0];
        float f7 = camPos[2];

        Mat4.perspective(projMatrix, fov, aspect, 0.25f, 400.0f);
        Mat4.lookAt(viewMatrix, camPos[0], camPos[1], camPos[2], f6, 100.0f, f7, 0.0f, 0.0f, 1.0f);
        Mat4.multiply(viewProjMatrix, projMatrix, viewMatrix);

        // Clear Screen
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);
        GLES20.glUseProgram(program);

        // Core Glow (Left, Center, Right)
        glowEmitter.update(dt, startColor, destColor, flameNoise, hotspot);
        renderEmitter(glowEmitter, POS_GLOW_L);
        renderEmitter(glowEmitter, POS_GLOW_C);
        renderEmitter(glowEmitter, POS_GLOW_R);

        // Main Fire (5 continuous columns covering the entire screen width)
        fireEmitter.update(dt, startColor, destColor, flameDensity, flameNoise, hotspot);
        renderEmitter(fireEmitter, POS_LL);
        renderEmitter(fireEmitter, POS_L);
        renderEmitter(fireEmitter, POS_C);
        renderEmitter(fireEmitter, POS_R);
        renderEmitter(fireEmitter, POS_RR);

        // Sparks (Left, Center, Right)
        sparksEmitter.update(dt, flameDensity, flameNoise, hotspot);
        renderEmitter(sparksEmitter, POS_SPARKS_L);
        renderEmitter(sparksEmitter, POS_SPARKS_C);
        renderEmitter(sparksEmitter, POS_SPARKS_R);


        // Touch bursts
        for (BurstEmitter b : burstEmitters) {
            if (b.active || b.activeCount > 0) {
                b.update(dt, startColor, destColor, hotspot);
                renderEmitter(b, b.customPos);
            }
        }
    }

    private void renderEmitter(Emitter emitter, float[] worldPos) {
        Mesh mesh = meshes.get(emitter.meshName);
        Integer tex = textures.get(emitter.textureName);
        if (mesh == null || tex == null) return;

        GLES20.glBlendFunc(emitter.blendSrc, emitter.blendDst);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, tex);
        GLES20.glUniform1i(u_s_diffuse, 0);

        Mat4.translate(mvpMatrix, viewProjMatrix, worldPos[0], worldPos[1], worldPos[2]);
        GLES20.glUniformMatrix4fv(u_mtx_modelviewproj, 1, false, mvpMatrix, 0);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mesh.vbo);
        GLES20.glVertexAttribPointer(a_position, 3, GLES20.GL_FLOAT, false, 0, 0);
        GLES20.glEnableVertexAttribArray(a_position);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mesh.uvbo);
        GLES20.glVertexAttribPointer(a_texcoord_0, 2, GLES20.GL_FLOAT, false, 0, 0);
        GLES20.glEnableVertexAttribArray(a_texcoord_0);

        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, mesh.ibo);

        for (int i = 0; i < emitter.maxParticles; i++) {
            Particle p = emitter.particles[i];
            if (!p.active) continue;

            GLES20.glUniform3fv(u_vec3_translation, 1, p.pos, 0);
            GLES20.glUniform3fv(u_vec3_scale, 1, p.curScale, 0);
            GLES20.glUniform1f(u_float_angle, p.angle);
            GLES20.glUniform4fv(u_color_0, 1, p.curColor, 0);

            GLES20.glDrawElements(GLES20.GL_TRIANGLES, mesh.count, GLES20.GL_UNSIGNED_SHORT, 0);
        }

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    private int loadTextureFromAssets(String path) {
        int[] tex = new int[1];
        GLES20.glGenTextures(1, tex, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, tex[0]);

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

        try (InputStream is = context.getAssets().open(path)) {
            Bitmap bmp = BitmapFactory.decodeStream(is);
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmp, 0);
            bmp.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tex[0];
    }

    private int compileShader(int type, String source) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, source);
        GLES20.glCompileShader(shader);
        return shader;
    }
}
