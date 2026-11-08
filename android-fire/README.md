# Ignis Live Wallpaper — Modern Android Live Wallpaper (1:1 Native Port)

Port nativo moderno en Java + OpenGL ES 2.0 del clásico fondo de pantalla animado de fuego.

## 🚀 Características
- **100% Nativo (Java + GLES 2.0):** Sin librerías obsoletas `.so` de 32-bits. Compatible con todas las arquitecturas de 64-bits (`arm64-v8a`, `x86_64`) y todas las versiones modernas de Android (Android 5.0 Lollipop hasta Android 15+).
- **Arquitectura 1:1:**
  - Emisores de partículas 3D exactos (`CoreGlow`, `Fire`, `Sparks`, `Burst`).
  - Shaders OpenGL ES 2.0 reales (`particle_basic_vs` con `ROTATE_Y` y `particle_basic_ps`).
  - Mallas de vértices binarias reales extraídas del APK (`fire1`, `fire2`, `plane_16x16`).
  - Cámara de perspectiva 3D con oscilación horizontal al deslizar escritorios.
  - Soporte automático de **Portrait** (FOV 60°) y **Landscape** (FOV 35°).
  - Efecto de toque interactivo (chispas al tocar la pantalla).
- **Servicio Live Wallpaper Oficial:** Aparece en el selector de fondos animados del sistema.
- **Launcher Activity de Vista Previa:** Puedes abrir la app directamente desde el cajón de aplicaciones para ver la animación y pulsar el botón **"Set as Live Wallpaper"**.

## 🛠️ Cómo compilar y generar el APK con Android Studio

1. Abre **Android Studio**.
2. Selecciona **Open** (Abrir proyecto) y navega a esta carpeta:
   `android-fire/`
3. Espera a que Gradle sincronice el proyecto automáticamente.
4. Conecta tu teléfono Android por USB (con Depuración USB activada) o inicia un emulador.
5. Haz clic en el botón verde **Run ▶** (o en el menú: **Build > Build Bundle(s) / APK(s) > Build APK(s)**).
6. El archivo APK se generará en:
   `app/build/outputs/apk/debug/app-debug.apk`

¡Listo para instalar en cualquier teléfono o tablet Android!
