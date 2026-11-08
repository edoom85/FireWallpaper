// ============================================================
// FISHNOODLE FIRE LIVE WALLPAPER — Native KDE Plasma 6 Edition
// 1:1 3D Particle system, GLES20 shaders, exact meshes & presets.
// Fully responsive for ultrawide (21:9) and dual monitor setups.
// ============================================================

const canvas = document.getElementById('fireCanvas');
const gl = canvas.getContext('webgl', {
    alpha: false,
    antialias: false,
    premultipliedAlpha: false,
    powerPreference: 'high-performance'
});

if (!gl) {
    console.error('WebGL is not supported.');
}

// 1. EXACT SHADERS (from particle_basic_vs & particle_basic_ps)
const VS_PARTICLE = `
attribute vec4 a_position;
attribute vec2 a_texcoord_0;

varying vec2 v_texcoord_0;

uniform mat4 u_mtx_modelviewproj;
uniform vec3 u_vec3_translation;
uniform vec3 u_vec3_scale;
uniform float u_float_angle;

void main() {
    float r = radians(u_float_angle);
    float rCos = cos(r);
    float rSin = sin(r);

    vec4 new_position;
    new_position.xyz = a_position.xyz * u_vec3_scale;
    new_position.w = a_position.w;

    float axisX = new_position.x * rCos - new_position.z * rSin;
    float axisY = new_position.y;
    float axisZ = new_position.x * rSin + new_position.z * rCos;

    new_position.x = axisX;
    new_position.y = axisY;
    new_position.z = axisZ;

    new_position.xyz += u_vec3_translation;

    gl_Position = u_mtx_modelviewproj * new_position;
    v_texcoord_0 = a_texcoord_0;
}
`;

const FS_PARTICLE = `
precision mediump float;

varying vec2 v_texcoord_0;

uniform sampler2D s_diffuse;
uniform vec4 u_color_0;

void main() {
    gl_FragColor = texture2D(s_diffuse, v_texcoord_0) * u_color_0;
}
`;

function createShader(gl, type, source) {
    const s = gl.createShader(type);
    gl.shaderSource(s, source);
    gl.compileShader(s);
    if (!gl.getShaderParameter(s, gl.COMPILE_STATUS)) {
        console.error('Shader error:', gl.getShaderInfoLog(s));
        gl.deleteShader(s);
        return null;
    }
    return s;
}

function createProgram(gl, vsSource, fsSource) {
    const vs = createShader(gl, gl.VERTEX_SHADER, vsSource);
    const fs = createShader(gl, gl.FRAGMENT_SHADER, fsSource);
    const prog = gl.createProgram();
    gl.attachShader(prog, vs);
    gl.attachShader(prog, fs);
    gl.linkProgram(prog);
    if (!gl.getProgramParameter(prog, gl.LINK_STATUS)) {
        console.error('Program link error:', gl.getProgramInfoLog(prog));
        return null;
    }
    return prog;
}

const particleProg = createProgram(gl, VS_PARTICLE, FS_PARTICLE);

const a_position          = gl.getAttribLocation(particleProg, 'a_position');
const a_texcoord_0        = gl.getAttribLocation(particleProg, 'a_texcoord_0');
const u_mtx_modelviewproj = gl.getUniformLocation(particleProg, 'u_mtx_modelviewproj');
const u_vec3_translation  = gl.getUniformLocation(particleProg, 'u_vec3_translation');
const u_vec3_scale        = gl.getUniformLocation(particleProg, 'u_vec3_scale');
const u_float_angle       = gl.getUniformLocation(particleProg, 'u_float_angle');
const u_color_0           = gl.getUniformLocation(particleProg, 'u_color_0');
const u_s_diffuse         = gl.getUniformLocation(particleProg, 's_diffuse');

// 2. EXACT 3D MESH GEOMETRY
const MESH_PLANE_16X16 = {
    positions: new Float32Array([
        8.0, 0.0, 8.0,
        -7.9998, 0.0, 8.0,
        -8.0, 0.0, -7.9998,
        8.0, 0.0, -8.0
    ]),
    uvs: new Float32Array([
        1.0, 0.0,
        0.0, 0.0,
        0.0, 1.0,
        1.0, 1.0
    ]),
    indices: new Uint16Array([0, 1, 3, 1, 2, 3])
};

const MESH_FIRE1 = {
    positions: new Float32Array([
        -3.9838, 0.0, -7.9998,
        8.0, 0.0, 5.2666,
        1.3522, 0.0, 8.0,
        -8.0, 0.0, -4.4906,
        8.0, 0.0, -3.5271,
        4.5735, 0.0, -7.9998,
        -3.3238, 0.0, 8.0,
        -8.0336, 0.0, 4.7253
    ]),
    uvs: new Float32Array([
        0.2523, 0.9993,
        1.0, 0.1708,
        0.5846, 0.0027,
        -0.0012, 0.8052,
        1.0, 0.7038,
        0.775, 0.9997,
        0.2198, 0.0039,
        0.0019, 0.2076
    ]),
    indices: new Uint16Array([
        0, 1, 3,
        3, 1, 2,
        2, 6, 3,
        3, 6, 7,
        4, 1, 5,
        1, 0, 5
    ])
};

const MESH_FIRE2 = {
    positions: new Float32Array([
        -3.2403, 1.7906, 5.0245,
        -3.2403, 1.7906, 0.7077,
        1.0762, 1.7906, 0.7077,
        1.0762, 1.7906, 5.0245,
        5.653, -2.9159, -3.6613,
        5.653, -2.9159, 2.4677,
        -0.476, -2.9159, 2.4677,
        -0.476, -2.9159, -3.6613,
        1.0951, -1.0845, -4.4251,
        1.0951, -1.0845, -0.1083,
        -3.2214, -1.0845, -0.1083,
        -3.2214, -1.0845, -4.4251,
        -9.1399, 0.0, 7.0124,
        -5.6699, 0.0, 4.4451,
        -3.1022, 0.0, 7.9155,
        -6.5726, 0.0, 10.4828,
        9.5526, -2.4229, -0.6356,
        6.2667, -2.4229, -3.4353,
        9.0664, -2.4229, -6.7212,
        12.3523, -2.4229, -3.9215,
        -4.4738, 4.7114, -2.1354,
        -7.3225, 4.7114, -5.3791,
        -4.0792, 4.7114, -8.2275,
        -1.2305, 4.7114, -4.9842,
        6.0298, 0.0, 0.5334,
        9.5968, 0.0, 2.9645,
        7.1656, 0.0, 6.5319,
        3.5983, 0.0, 4.1007
    ]),
    uvs: new Float32Array([
        0.517, 0.0137,  0.517, 0.4726,  0.976, 0.4726,  0.976, 0.0137,
        0.9537, 0.9856, 0.9537, 0.5314, 0.4996, 0.5314, 0.4996, 0.9856,
        0.4658, 0.9798, 0.4658, 0.5349, 0.0209, 0.5349, 0.0209, 0.9798,
        0.976, 0.4726,  0.976, 0.0137,  0.517, 0.0137,  0.517, 0.4726,
        0.4658, 0.9798, 0.4658, 0.5349, 0.0209, 0.5349, 0.0209, 0.9798,
        0.4658, 0.9798, 0.4658, 0.5349, 0.0209, 0.5349, 0.0209, 0.9798,
        0.976, 0.4726,  0.976, 0.0137,  0.517, 0.0137,  0.517, 0.4726
    ]),
    indices: new Uint16Array([
        0, 1, 3,  3, 1, 2,
        4, 5, 7,  7, 5, 6,
        8, 9, 11, 11, 9, 10,
        12, 13, 15, 15, 13, 14,
        16, 17, 19, 19, 17, 18,
        20, 21, 23, 23, 21, 22,
        24, 25, 27, 25, 26, 27
    ])
};

function initMeshGL(meshData) {
    const vbo = gl.createBuffer();
    gl.bindBuffer(gl.ARRAY_BUFFER, vbo);
    gl.bufferData(gl.ARRAY_BUFFER, meshData.positions, gl.STATIC_DRAW);

    const uvbo = gl.createBuffer();
    gl.bindBuffer(gl.ARRAY_BUFFER, uvbo);
    gl.bufferData(gl.ARRAY_BUFFER, meshData.uvs, gl.STATIC_DRAW);

    const ibo = gl.createBuffer();
    gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, ibo);
    gl.bufferData(gl.ELEMENT_ARRAY_BUFFER, meshData.indices, gl.STATIC_DRAW);

    return { vbo, uvbo, ibo, count: meshData.indices.length };
}

const meshes = {
    'plane_16x16': initMeshGL(MESH_PLANE_16X16),
    'fire1':       initMeshGL(MESH_FIRE1),
    'fire2':       initMeshGL(MESH_FIRE2)
};

// 3. TEXTURE MANAGEMENT
const textures = {};

function loadTexture(name, filename) {
    const tex = gl.createTexture();
    gl.bindTexture(gl.TEXTURE_2D, tex);
    gl.texImage2D(gl.TEXTURE_2D, 0, gl.RGBA, 1, 1, 0, gl.RGBA, gl.UNSIGNED_BYTE, new Uint8Array([0, 0, 0, 0]));

    const img = new Image();
    img.onload = () => {
        gl.bindTexture(gl.TEXTURE_2D, tex);
        gl.pixelStorei(gl.UNPACK_FLIP_Y_WEBGL, false);
        gl.texImage2D(gl.TEXTURE_2D, 0, gl.RGBA, gl.RGBA, gl.UNSIGNED_BYTE, img);
        gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_S, gl.CLAMP_TO_EDGE);
        gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_T, gl.CLAMP_TO_EDGE);
        gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MIN_FILTER, gl.LINEAR);
        gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MAG_FILTER, gl.LINEAR);
    };
    img.src = 'textures/' + filename;
    textures[name] = tex;
}

const textureFiles = {
    'fire1': 'fire1_ktx.png',
    'fire2': 'fire2_ktx.png',
    'fire3': 'fire3_ktx.png',
    'fire4': 'fire4_ktx.png',
    'hdfire1': 'hdfire1_ktx.png',
    'hdfire2': 'hdfire2_ktx.png',
    'hdfire3': 'hdfire3_ktx.png',
    'hdfire4': 'hdfire4_ktx.png',
    'bfire1': 'bfire1_ktx.png',
    'bfire3': 'bfire3_ktx.png',
    'bfire4': 'bfire4_ktx.png',
    'gfire1': 'gfire1_ktx.png',
    'gfire3': 'gfire3_ktx.png',
    'gfire4': 'gfire4_ktx.png',
    'wfire1': 'wfire1_ktx.png',
    'wfire3': 'wfire3_ktx.png',
    'wfire4': 'wfire4_ktx.png'
};

for (const [k, v] of Object.entries(textureFiles)) {
    loadTexture(k, v);
}

// 4. MATRIX OPERATIONS
function mat4_create() {
    return new Float32Array(16);
}

function mat4_perspective(out, fovY_deg, aspect, near, far) {
    const tan = near * Math.tan(0.008726646 * fovY_deg);
    const right = tan * aspect;
    const top = tan;
    out.fill(0);
    out[0] = near / right;
    out[5] = near / top;
    out[10] = (far + near) / (near - far);
    out[11] = -1.0;
    out[14] = (2.0 * far * near) / (near - far);
    return out;
}

function mat4_lookAt(out, eyeX, eyeY, eyeZ, targetX, targetY, targetZ, upX, upY, upZ) {
    const f11 = targetX - eyeX;
    const f12 = targetY - eyeY;
    const f13 = targetZ - eyeZ;
    const len = 1.0 / Math.hypot(f11, f12, f13);
    const f14 = f11 * len;
    const f15 = f12 * len;
    const f16 = f13 * len;
    const f17 = (f15 * upZ) - (f16 * upY);
    const f18 = (f16 * upX) - (f14 * upZ);
    const f19 = (f14 * upY) - (f15 * upX);
    const len2 = 1.0 / Math.hypot(f17, f18, f19);
    const f20 = f17 * len2;
    const f21 = f18 * len2;
    const f22 = f19 * len2;

    out[0] = f20;
    out[1] = (f21 * f16) - (f22 * f15);
    out[2] = -f14;
    out[3] = 0.0;
    out[4] = f21;
    out[5] = (f22 * f14) - (f20 * f16);
    out[6] = -f15;
    out[7] = 0.0;
    out[8] = f22;
    out[9] = (f20 * f15) - (f21 * f14);
    out[10] = -f16;
    out[11] = 0.0;
    out[12] = -(out[0] * eyeX + out[4] * eyeY + out[8] * eyeZ);
    out[13] = -(out[1] * eyeX + out[5] * eyeY + out[9] * eyeZ);
    out[14] = -(out[2] * eyeX + out[6] * eyeY + out[10] * eyeZ);
    out[15] = 1.0;
    return out;
}

function mat4_multiply(out, a, b) {
    for (let i = 0; i < 4; i++) {
        const ai0 = a[i], ai1 = a[i+4], ai2 = a[i+8], ai3 = a[i+12];
        out[i]    = ai0 * b[0]  + ai1 * b[1]  + ai2 * b[2]  + ai3 * b[3];
        out[i+4]  = ai0 * b[4]  + ai1 * b[5]  + ai2 * b[6]  + ai3 * b[7];
        out[i+8]  = ai0 * b[8]  + ai1 * b[9]  + ai2 * b[10] + ai3 * b[11];
        out[i+12] = ai0 * b[12] + ai1 * b[13] + ai2 * b[14] + ai3 * b[15];
    }
    return out;
}

function mat4_translate(out, m, tx, ty, tz) {
    for (let i = 0; i < 12; i++) out[i] = m[i];
    out[12] = m[0] * tx + m[4] * ty + m[8] * tz + m[12];
    out[13] = m[1] * tx + m[5] * ty + m[9] * tz + m[13];
    out[14] = m[2] * tx + m[6] * ty + m[10] * tz + m[14];
    out[15] = m[3] * tx + m[7] * ty + m[11] * tz + m[15];
    return out;
}

// 5. PARTICLE PHYSICS & EMITTERS
function randRange(min, max) {
    return min + Math.random() * (max - min);
}

class Particle {
    constructor() {
        this.startVel   = [0, 0, 0];
        this.endVel     = [0, 0, 0];
        this.startScale = [1, 1, 1];
        this.endScale   = [1, 1, 1];
        this.startAngle = 0;
        this.endAngle   = 0;
        this.angVel     = 0;
        this.lifetime   = 1;
        this.active     = false;

        this.curVel   = [0, 0, 0];
        this.curScale = [1, 1, 1];
        this.curColor = [1, 1, 1, 1];
        this.pos      = [0, 0, 0];
        this.angle    = 0;
        this.age      = 0;

        this.hasAngleLerp = false;
        this.hasScaleLerp = false;
    }

    reset() {
        this.pos = [0, 0, 0];
        this.age = 0;
        this.startVel = [0, 0, 0];
        this.endVel   = [0, 0, 0];
        this.startScale = [1, 1, 1];
        this.endScale   = [1, 1, 1];
        this.startAngle = 0;
        this.endAngle   = 0;
        this.angVel     = 0;
        this.lifetime   = 1;
    }

    init() {
        if (this.startAngle !== this.endAngle) {
            this.hasAngleLerp = true;
        } else {
            this.hasAngleLerp = false;
            this.angle = this.startAngle;
        }
        if (this.startScale[0] === 1 && this.startScale[1] === 1 && this.startScale[2] === 1 &&
            this.endScale[0] === 1 && this.endScale[1] === 1 && this.endScale[2] === 1) {
            this.hasScaleLerp = false;
        } else {
            this.hasScaleLerp = true;
        }
    }

    update(dt, emitter) {
        this.age += dt;
        if (this.age > this.lifetime) {
            this.active = false;
            return false;
        }
        const f2 = this.age / this.lifetime;
        const f3 = 1.0 - f2;

        this.curVel[0] = this.startVel[0] * f3 + this.endVel[0] * f2;
        this.curVel[1] = this.startVel[1] * f3 + this.endVel[1] * f2;
        this.curVel[2] = this.startVel[2] * f3 + this.endVel[2] * f2;

        this.curColor[0] = emitter.colorStart[0] * f3 + emitter.colorEnd[0] * f2;
        this.curColor[1] = emitter.colorStart[1] * f3 + emitter.colorEnd[1] * f2;
        this.curColor[2] = emitter.colorStart[2] * f3 + emitter.colorEnd[2] * f2;
        this.curColor[3] = emitter.colorStart[3] * f3 + emitter.colorEnd[3] * f2;

        if (this.hasScaleLerp) {
            this.curScale[0] = this.startScale[0] * f3 + this.endScale[0] * f2;
            this.curScale[1] = this.startScale[1] * f3 + this.endScale[1] * f2;
            this.curScale[2] = this.startScale[2] * f3 + this.endScale[2] * f2;
        }

        if (this.hasAngleLerp) {
            this.angle = f2 * this.endAngle + f3 * this.startAngle;
        } else {
            this.angle += this.angVel * dt;
        }

        this.pos[0] += this.curVel[0] * dt;
        this.pos[1] += this.curVel[1] * dt;
        this.pos[2] += this.curVel[2] * dt;

        return true;
    }
}

class Emitter {
    constructor() {
        this.colorStart = [1, 1, 1, 1];
        this.colorEnd   = [1, 1, 1, 1];
        this.meshName   = 'plane_16x16';
        this.textureName= 'fire1';
        this.spawnRate  = 1.0;
        this.spawnJitter= 0.2;
        this.burstCount = 0;
        this.posJitterX = 0;
        this.posJitterY = 0;
        this.posJitterZ = 0;
        this.active     = true;
        this.timerX     = 0;
        this.timerY     = 0;
        this.activeCount= 0;
        this.maxParticles = 36;
        this.blendFunc  = [gl.SRC_ALPHA, gl.ONE];

        this.particles = [];
        for (let i = 0; i < this.maxParticles; i++) {
            this.particles.push(new Particle());
        }
    }

    setupParticle(p) {
        p.reset();
        p.pos[0] = this.posJitterX > 0.01 ? randRange(-this.posJitterX, this.posJitterX) : 0;
        p.pos[1] = this.posJitterY > 0.01 ? randRange(-this.posJitterY, this.posJitterY) : 0;
        p.pos[2] = this.posJitterZ > 0.01 ? randRange(-this.posJitterZ, this.posJitterZ) : 0;
        p.active = true;
    }

    update(dt) {
        let toSpawn = 0;
        if (this.active) {
            if (this.burstCount > 0) {
                toSpawn = this.burstCount;
                this.active = false;
            } else if (this.activeCount < this.maxParticles) {
                this.timerX += dt;
                while ((this.timerX + this.timerY) >= this.spawnRate) {
                    this.timerX -= (this.spawnRate + this.timerY);
                    this.timerY = this.spawnJitter > 0 ? randRange(-this.spawnJitter, this.spawnJitter) : 0;
                    toSpawn++;
                }
            }
        }

        for (let i = 0; i < this.particles.length; i++) {
            const p = this.particles[i];
            if (p.active) {
                if (!p.update(dt, this)) {
                    this.activeCount--;
                }
            } else if (toSpawn > 0) {
                let initialAge = 0.001;
                if (toSpawn > 1 && this.burstCount === 0) {
                    initialAge = (toSpawn - 1) * this.spawnRate;
                }
                this.setupParticle(p);
                p.init();
                p.update(initialAge, this);
                this.activeCount++;
                toSpawn--;
            }
        }
    }
}

// Global parameters
let flameDensity   = 0.5;
let flameNoise     = 0.5;
let speedMultiplier= 1.0;
let sparkRateMult  = 1.0;
let hotspot        = 'fire';
let startColor     = [1.0, 0.75, 0.3, 1.0];
let destColor      = [1.0, 0.27, 0.27, 1.0];
let bgColor        = [0.0, 0.0, 0.0, 1.0];
let spreadMode     = 'auto';

// Core Glow Emitter
class CoreGlowEmitter extends Emitter {
    constructor() {
        super();
        this.spawnRate = 0.1;
        this.spawnJitter = 0.0;
        this.meshName = 'plane_16x16';
        this.textureName = 'fire4';
        this.colorStart = [1.0, 1.0, 1.0, 3.0];
        this.colorEnd   = [1.0, 1.0, 1.0, 0.0];
        this.posJitterX = 12.0;
        this.posJitterY = 0.5;
        this.blendFunc  = [gl.SRC_ALPHA, gl.ONE];
    }

    update(dt) {
        this.colorStart[0] = startColor[0] * startColor[0];
        this.colorStart[1] = startColor[1] * startColor[1];
        this.colorStart[2] = startColor[2] * startColor[2];
        this.colorStart[3] = 3.0;

        this.colorEnd[0] = destColor[0];
        this.colorEnd[1] = destColor[1];
        this.colorEnd[2] = destColor[2];
        this.colorEnd[3] = 0.0;

        this.textureName = (hotspot === 'bfire' || hotspot === 'gfire' || hotspot === 'wfire' || hotspot === 'hdfire') ?
            (hotspot + '4') : 'fire4';

        super.update(dt * speedMultiplier);
    }

    setupParticle(p) {
        super.setupParticle(p);
        p.lifetime = 3.0;
        const s = randRange(0.01, 0.05);
        p.startScale = [s, s, s];
        const sEnd = randRange(0.01, 0.05);
        p.endScale = [sEnd, sEnd, sEnd];

        const a1 = randRange(-5.0, 5.0) * flameNoise;
        const a2 = randRange(-10.0, 10.0) * flameNoise;
        p.startVel = [a1, 0.0, 6.0];
        p.endVel   = [a2, 0.0, 10.0];
        p.startAngle = randRange(-360.0, 360.0);
        p.endAngle   = randRange(-360.0, 360.0);
    }
}

// Main Flame Emitter
class FireEmitter extends Emitter {
    constructor() {
        super();
        this.meshName = 'fire1';
        this.spawnRate = 0.06;
        this.spawnJitter = 0.0;
        this.textureName = 'fire1';
        this.colorStart = [1.0, 1.0, 1.0, 1.0];
        this.colorEnd   = [1.0, 0.25, 0.25, 0.0];
        this.posJitterX = 5.5;
        this.posJitterY = 1.0;
        this.blendFunc  = [gl.SRC_ALPHA, gl.ONE];
    }

    update(dt) {
        this.colorStart[0] = startColor[0];
        this.colorStart[1] = startColor[1];
        this.colorStart[2] = startColor[2];
        this.colorStart[3] = 1.0;

        this.colorEnd[0] = destColor[0];
        this.colorEnd[1] = destColor[1];
        this.colorEnd[2] = destColor[2];
        this.colorEnd[3] = 0.0;

        this.spawnRate = (((1.0 - flameDensity) * 0.09) + 0.05) / Math.max(0.2, speedMultiplier);
        this.textureName = (hotspot === 'bfire' || hotspot === 'gfire' || hotspot === 'wfire' || hotspot === 'hdfire') ?
            (hotspot + '1') : 'fire1';

        super.update(dt * speedMultiplier);
    }

    setupParticle(p) {
        super.setupParticle(p);
        p.lifetime = 1.25;
        const s = randRange(0.4, 0.8);
        p.startScale = [s, s, s];
        const sEnd = randRange(0.2, 0.4);
        p.endScale = [sEnd, sEnd, sEnd];

        const a1 = randRange(-5.0, 5.0) * flameNoise;
        const a2 = randRange(-15.0, 15.0) * flameNoise;
        p.startVel = [a1, 0.0, 14.0];
        p.endVel   = [a2, 0.0, 22.0];
        p.startAngle = 0;
        p.endAngle = 0;
    }
}

// Sparks Emitter
class SparksEmitter extends Emitter {
    constructor() {
        super();
        this.meshName = 'fire2';
        this.spawnRate = 0.06;
        this.spawnJitter = 0.0;
        this.textureName = 'fire2';
        this.colorStart = [1.66, 1.66, 1.66, 1.0];
        this.colorEnd   = [0.0, 0.0, 0.0, 0.0];
        this.posJitterX = 14.0;
        this.posJitterY = 1.0;
        this.blendFunc  = [gl.DST_COLOR, gl.ONE];
    }

    update(dt) {
        if (sparkRateMult <= 0.01) {
            this.active = false;
            return;
        }
        this.active = true;
        this.spawnRate = (((1.0 - flameDensity) * 0.08) + 0.04) / Math.max(0.1, sparkRateMult);
        this.textureName = (hotspot === 'hdfire') ? 'hdfire2' : 'fire2';
        super.update(dt * speedMultiplier);
    }

    setupParticle(p) {
        super.setupParticle(p);
        p.lifetime = 1.5;
        const s = randRange(0.12, 0.9);
        p.startScale = [s, s, s];
        const sEnd = randRange(0.25, 0.5);
        p.endScale = [sEnd, sEnd, sEnd];

        const a1 = randRange(-5.0, 5.0) * flameNoise;
        const a2 = randRange(-15.0, 15.0) * flameNoise;
        p.startVel = [a1, 0.0, 10.0];
        p.endVel   = [a2, 0.0, 18.0];
        p.startAngle = randRange(-180.0, 180.0);
        p.endAngle   = p.startAngle + randRange(-90.0, 90.0);
    }
}

// Instantiate emitters
const glowEmitter   = new CoreGlowEmitter();
const fireEmitter   = new FireEmitter();
const sparksEmitter = new SparksEmitter();

// 6. RENDER PASSES & CAMERA
const projMatrix     = mat4_create();
const viewMatrix     = mat4_create();
const viewProjMatrix = mat4_create();
const mvpMatrix      = mat4_create();

let camPos = [0.0, 0.0, 0.0];
let targetCamPos = [0.0, 0.0, 0.0];
let camSpeed = 1.0;

function resizeCanvas() {
    const dpr = window.devicePixelRatio || 1.0;
    const displayWidth = Math.round(window.innerWidth * dpr);
    const displayHeight = Math.round(window.innerHeight * dpr);

    if (canvas.width !== displayWidth || canvas.height !== displayHeight) {
        canvas.width = displayWidth;
        canvas.height = displayHeight;
    }
}

window.addEventListener('resize', resizeCanvas);
resizeCanvas();

function getFlameColumns(aspect) {
    if (spreadMode === '3col') {
        return [
            [-7.0, 20.0, -13.0],
            [ 0.0, 20.0, -13.0],
            [ 7.0, 20.0, -13.0]
        ];
    }
    if (spreadMode === '7col') {
        return [
            [-21.0, 20.0, -13.0],
            [-14.0, 20.0, -13.0],
            [ -7.0, 20.0, -13.0],
            [  0.0, 20.0, -13.0],
            [  7.0, 20.0, -13.0],
            [ 14.0, 20.0, -13.0],
            [ 21.0, 20.0, -13.0]
        ];
    }
    // Default 5 columns (natural, balanced spacing)
    return [
        [-14.0, 20.0, -13.0],
        [ -7.0, 20.0, -13.0],
        [  0.0, 20.0, -13.0],
        [  7.0, 20.0, -13.0],
        [ 14.0, 20.0, -13.0]
    ];
}

function updateCamera(dt) {
    const isLandscape = canvas.width >= canvas.height;
    const aspect = canvas.width / canvas.height;
    // On ultrawide (21:9), tune FOV from 35 to 28 so flames rise taller and maintain natural vertical proportions
    const fov = isLandscape ? (aspect > 2.0 ? 28.0 : 35.0) : 60.0;

    targetCamPos[0] = 0.0;
    targetCamPos[1] = 0.0;
    targetCamPos[2] = 0.0;

    const f2 = 5.0 * dt * camSpeed;
    camPos[0] += (targetCamPos[0] - camPos[0]) * f2;
    camPos[1] += (targetCamPos[1] - camPos[1]) * f2;
    camPos[2] += (targetCamPos[2] - camPos[2]) * f2;

    mat4_perspective(projMatrix, fov, aspect, 0.25, 400.0);
    mat4_lookAt(viewMatrix, camPos[0], camPos[1], camPos[2], camPos[0], 100.0, camPos[2], 0.0, 0.0, 1.0);
    mat4_multiply(viewProjMatrix, projMatrix, viewMatrix);
}

function renderEmitter(emitter, worldPos) {
    const mesh = meshes[emitter.meshName];
    const tex = textures[emitter.textureName];
    if (!mesh || !tex) return;

    gl.blendFunc(emitter.blendFunc[0], emitter.blendFunc[1]);

    gl.activeTexture(gl.TEXTURE0);
    gl.bindTexture(gl.TEXTURE_2D, tex);
    gl.uniform1i(u_s_diffuse, 0);

    mat4_translate(mvpMatrix, viewProjMatrix, worldPos[0], worldPos[1], worldPos[2]);
    gl.uniformMatrix4fv(u_mtx_modelviewproj, false, mvpMatrix);

    gl.bindBuffer(gl.ARRAY_BUFFER, mesh.vbo);
    gl.vertexAttribPointer(a_position, 3, gl.FLOAT, false, 0, 0);
    gl.enableVertexAttribArray(a_position);

    gl.bindBuffer(gl.ARRAY_BUFFER, mesh.uvbo);
    gl.vertexAttribPointer(a_texcoord_0, 2, gl.FLOAT, false, 0, 0);
    gl.enableVertexAttribArray(a_texcoord_0);

    gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, mesh.ibo);

    for (let i = 0; i < emitter.particles.length; i++) {
        const p = emitter.particles[i];
        if (!p.active) continue;

        gl.uniform3fv(u_vec3_translation, p.pos);
        gl.uniform3fv(u_vec3_scale, p.curScale);
        gl.uniform1f(u_float_angle, p.angle);
        gl.uniform4fv(u_color_0, p.curColor);

        gl.drawElements(gl.TRIANGLES, mesh.count, gl.UNSIGNED_SHORT, 0);
    }
}

// 7. MAIN RENDER LOOP
let lastTime = performance.now();

function renderFrame(now) {
    requestAnimationFrame(renderFrame);

    const elapsed = now - lastTime;
    const dt = Math.min(elapsed / 1000.0, 0.1);
    lastTime = now;

    resizeCanvas();

    // 1. Camera
    updateCamera(dt);

    // 2. Clear Screen
    gl.viewport(0, 0, canvas.width, canvas.height);
    gl.clearColor(bgColor[0], bgColor[1], bgColor[2], bgColor[3]);
    gl.clear(gl.COLOR_BUFFER_BIT);

    // 3. Blend & Program
    gl.enable(gl.BLEND);
    gl.disable(gl.DEPTH_TEST);
    gl.useProgram(particleProg);

    const aspect = canvas.width / canvas.height;
    const flameCols = getFlameColumns(aspect);

    // 4. Glow (3 focal cores)
    glowEmitter.update(dt);
    renderEmitter(glowEmitter, [-6.0, 20.0, -13.0]);
    renderEmitter(glowEmitter, [ 0.0, 20.0, -13.0]);
    renderEmitter(glowEmitter, [ 6.0, 20.0, -13.0]);

    // 5. Main Fire
    fireEmitter.update(dt);
    for (let i = 0; i < flameCols.length; i++) {
        renderEmitter(fireEmitter, flameCols[i]);
    }

    // 6. Sparks (3 focal cores)
    sparksEmitter.update(dt);
    renderEmitter(sparksEmitter, [-5.0, 20.0, -14.0]);
    renderEmitter(sparksEmitter, [ 0.0, 20.0, -14.0]);
    renderEmitter(sparksEmitter, [ 5.0, 20.0, -14.0]);
}

requestAnimationFrame(renderFrame);

// 8. PRESETS
const PRESETS = {
    'classic': {
        hotspot: 'fire',
        startColor: [1.0, 0.75, 0.3, 1.0],
        destColor:  [1.0, 0.27, 0.27, 1.0],
        bg: [0.0, 0.0, 0.0, 1.0]
    },
    'blue': {
        hotspot: 'bfire',
        startColor: [0.3, 0.75, 1.0, 1.0],
        destColor:  [0.1, 0.3, 0.9, 1.0],
        bg: [0.0, 0.0, 0.0, 1.0]
    },
    'emerald': {
        hotspot: 'gfire',
        startColor: [0.3, 1.0, 0.4, 1.0],
        destColor:  [0.05, 0.6, 0.1, 1.0],
        bg: [0.0, 0.0, 0.0, 1.0]
    },
    'purple': {
        hotspot: 'fire',
        startColor: [0.9, 0.3, 1.0, 1.0],
        destColor:  [0.5, 0.05, 0.7, 1.0],
        bg: [0.0, 0.0, 0.0, 1.0]
    },
    'cyan': {
        hotspot: 'wfire',
        startColor: [0.3, 0.9, 1.0, 1.0],
        destColor:  [0.1, 0.5, 0.8, 1.0],
        bg: [0.0, 0.0, 0.0, 1.0]
    },
    'toxic': {
        hotspot: 'gfire',
        startColor: [0.8, 1.0, 0.2, 1.0],
        destColor:  [0.2, 0.6, 0.0, 1.0],
        bg: [0.0, 0.0, 0.0, 1.0]
    }
};

function applyPreset(presetKey) {
    const p = PRESETS[presetKey];
    if (!p) return;
    hotspot    = p.hotspot;
    startColor = [...p.startColor];
    destColor  = [...p.destColor];
    bgColor    = [...p.bg];
}

// 9. PLASMA CONFIGURATION BRIDGE
window.applyPlasmaConfig = function(cfg) {
    if (!cfg) return;
    if (cfg.preset && PRESETS[cfg.preset]) {
        applyPreset(cfg.preset);
    }
    if (typeof cfg.speed !== 'undefined') {
        speedMultiplier = Number(cfg.speed) / 100.0;
    }
    if (typeof cfg.sparks !== 'undefined') {
        sparkRateMult = Number(cfg.sparks) / 100.0;
    }
    if (cfg.spread) {
        spreadMode = cfg.spread;
    }
};

