# 🔥 Fishnoodle Fire — Multi-Platform Live Wallpaper

Recreación moderna, fiel y de alto rendimiento (1:1) del clásico fondo animado 3D de fuego (*Fishnoodle Fire Live Wallpaper*), portada para múltiples plataformas y entornos modernos.

---

## 📦 Estructura del Repositorio

El proyecto incluye cuatro implementaciones optimizadas que comparten la misma lógica matemática, shaders OpenGL ES / WebGL y mallas 3D:

| Directorio | Plataforma / Entorno | Tecnologías | Descripción |
| :--- | :--- | :--- | :--- |
| **[`android-fire/`](android-fire/)** | Android 5.0 a 15+ | Java 8/17, GLES 2.0 | Port nativo a 64-bits (`arm64-v8a`, `x86_64`) compatible con Android Studio moderno. |
| **[`plasma-wallpaper/`](plasma-wallpaper/)** | Linux (KDE Plasma 6) | QML (Qt 6), WebEngine | Plugin nativo para el escritorio Plasma 6 con panel de configuración Kirigami integrado. |
| **[`wallpaper-engine/`](wallpaper-engine/)** | Wallpaper Engine / Steam | HTML5, WebGL, JSON API | Paquete listo para Wallpaper Engine con sliders y selección de presets. |
| **[`web-simulator/`](web-simulator/)** | Navegadores Web | Vanilla JS, WebGL | Simulador de pruebas para depurar en tiempo real el render y shaders. |

---

## ✨ Características Principales

- **Fidelidad Visual 1:1:**
  - Emisores de partículas 3D idénticos: `CoreGlow`, `Fire`, `Sparks`, y `Burst` (interacción táctil/ratón).
  - Shaders OpenGL ES 2.0 / WebGL con rotación axial en Y (`ROTATE_Y`), perturbación UV y mezcla aditiva.
  - Mallas de geometría reales extraídas de la versión original (`fire1`, `fire2`, plano 16x16).
- **Soporte para Pantallas Ultrawide (21:9 / 32:9) y Multi-Monitor:**
  - Sistema de columnas dinámicas que evita la distorsión horizontal o fuego recortado en monitores ultrapanorámicos (como 3440×1440) y setups de 2 o más pantallas.
- **Paletas de Color y Presets:**
  - Fuego Clásico (Ámbar / Naranja / Rojo)
  - Fuego Azul Ártico / Hielo
  - Fuego Esmeralda / Tóxico
  - Fuego Púrpura / Místico
  - Modo Personalizado (selector RGB)
- **Rendimiento y Eficiencia:**
  - Renderizado acelerado 100% por hardware (GPU).
  - Sin dependencias pesadas ni librerías de 32-bits obsoletas.

---

## 🚀 Guías de Instalación y Uso

### 1. 🐧 KDE Plasma 6 (Linux Nativo)

Para instalar el plugin nativo de KDE Plasma 6 en tu sistema (`~/.local/share/plasma/wallpapers/`):

```bash
chmod +x install-plasma-wallpaper.sh
./install-plasma-wallpaper.sh
```

**Para activarlo:**
1. Haz clic derecho en tu escritorio y selecciona **Configurar fondo de pantalla y escritorio**.
2. En **Tipo de fondo del escritorio**, selecciona **Fishnoodle Fire**.
3. Elige tu paleta de color preferida, velocidad y distribución de llamas en el diálogo de configuración nativo y pulsa **Aplicar**.

---

### 2. 🎮 Wallpaper Engine (Steam / Linux KDE)

Si utilizas el plugin de Wallpaper Engine para KDE Plasma o la aplicación en Steam:

```bash
chmod +x install-wallpaper-engine.sh
./install-wallpaper-engine.sh
```

El script vinculará el proyecto tanto con el Workshop local de Steam (`431960/fishnoodle_fire`) como con la biblioteca local portable del proyecto.

---

### 3. 📱 Android Live Wallpaper (Ignis 3D)

1. **Instalación directa:** Puedes instalar el APK compilado directamente en tu teléfono:
   - [`ignis-live-wallpaper.apk`](file:///home/edoom/Documentos/Wallpaper/ignis-live-wallpaper.apk) (o en `android-fire/app/build/outputs/apk/debug/app-debug.apk`)
2. **Desde Android Studio:**
   - Abre **Android Studio**.
   - Selecciona **Open** y elige la carpeta `android-fire/`.
   - Conecta tu dispositivo Android (o abre un emulador) y pulsa **Run ▶**.
   - La aplicación incluye una pantalla de vista previa interactiva con el botón **"Set as Live Wallpaper"**.

---

### 4. 🌐 Simulador Web

Puedes probar el renderizado directamente en cualquier navegador ejecutando un servidor HTTP local en la raíz o dentro de `web-simulator/`:

```bash
cd web-simulator
python3 -m http.server 8080
```

Abre `http://localhost:8080` en tu navegador web.

---

## 🛠️ Tecnologías Empleadas

- **Lenguajes:** Java, JavaScript (ES6+), GLSL (OpenGL Shading Language), QML (Qt 6).
- **APIs Gráficas:** OpenGL ES 2.0 / WebGL 1.0/2.0.
- **Herramientas de Construcción:** Gradle (Android), KPackage / CMake (Plasma 6).

---

## 📄 Licencia

Este proyecto está distribuido bajo la licencia GPLv2+ / MIT con propósitos educativos y de preservación histórica de fondos animados clásicos de Android.

