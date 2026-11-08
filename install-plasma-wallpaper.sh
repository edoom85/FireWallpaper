#!/usr/bin/env bash
# ============================================================
# Fishnoodle Fire — Native KDE Plasma 6 Wallpaper Installer
# ============================================================
set -e

DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
TARGET_DIR="$HOME/.local/share/plasma/wallpapers/com.fishnoodle.fire"

echo "🔥 Instalando Fishnoodle Fire como plugin nativo de KDE Plasma 6..."

mkdir -p "$HOME/.local/share/plasma/wallpapers"
ln -sfn "$DIR/plasma-wallpaper/com.fishnoodle.fire" "$TARGET_DIR"

echo "✅ Enlazado en: $TARGET_DIR"
if command -v kpackagetool6 &>/dev/null; then
    if kpackagetool6 --type Plasma/Wallpaper --list | grep "com.fishnoodle.fire" >/dev/null; then
        echo "✅ Plugin registrado correctamente en KDE Plasma 6."
    fi
fi

echo ""
echo "============================================================"
echo "🎉 ¡Instalación completa!"
echo ""
echo "Para activarlo:"
echo " 1. Clic derecho en el escritorio -> 'Configurar fondo de pantalla y escritorio'."
echo " 2. En 'Tipo de fondo del escritorio', selecciona: 'Fishnoodle Fire'."
echo " 3. Elige tu paleta de color favorita y haz clic en 'Aplicar'."
echo "============================================================"

