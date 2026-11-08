#!/usr/bin/env bash
# ============================================================
# Fishnoodle Fire — Wallpaper Engine Installer for KDE Plasma 6
# ============================================================
set -e

DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
WP_SOURCE="$DIR/wallpaper-engine"
WORKSHOP_ID="fishnoodle_fire"

echo "🔥 Configurando Fishnoodle Fire para Wallpaper Engine en KDE Plasma 6..."

# 1. Crear estructura estándar en Steam local si existe o en ~/.local/share/Steam
STEAM_WORKSHOP="$HOME/.local/share/Steam/steamapps/workshop/content/431960"
mkdir -p "$STEAM_WORKSHOP" 2>/dev/null || true

if [ -d "$STEAM_WORKSHOP" ]; then
    ln -sfn "$WP_SOURCE" "$STEAM_WORKSHOP/$WORKSHOP_ID"
    echo "✅ Vinculado en biblioteca Steam: $STEAM_WORKSHOP/$WORKSHOP_ID"
else
    echo "ℹ️  No se pudo escribir en $STEAM_WORKSHOP directamente."
fi

# 2. Mantener la biblioteca local portable
LIB_DIR="$DIR/wallpaper-library"
mkdir -p "$LIB_DIR/steamapps/workshop/content/431960"
ln -sfn "$WP_SOURCE" "$LIB_DIR/steamapps/workshop/content/431960/$WORKSHOP_ID"
mkdir -p "$LIB_DIR/steamapps/common/wallpaper_engine/projects/myprojects"
ln -sfn "$WP_SOURCE" "$LIB_DIR/steamapps/common/wallpaper_engine/projects/myprojects/$WORKSHOP_ID"

echo "✅ Biblioteca local lista en: $LIB_DIR"
echo ""
echo "============================================================"
echo "🎉 ¡Instalación completa!"
echo ""
echo "Para activarlo en tu escritorio KDE Plasma:"
echo " 1. Clic derecho en el escritorio -> 'Configurar fondo de pantalla'."
echo " 2. Asegúrate de que el tipo de fondo sea 'Wallpaper Engine for Kde'."
echo " 3. En la esquina superior, haz clic en el icono de carpeta 'Library'."
echo " 4. Selecciona la carpeta:"
echo "    $LIB_DIR"
echo "    (o tu carpeta de Steam si ya la tenías vinculada)"
echo " 5. Verás aparecer 'Fishnoodle Fire Live Wallpaper' con su miniatura."
echo " 6. ¡Haz clic en él y disfruta del fuego a 60 FPS acelerado por GPU!"
echo "============================================================"

