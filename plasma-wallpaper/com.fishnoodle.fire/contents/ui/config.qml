import QtQuick
import QtQuick.Controls as QQC2
import QtQuick.Layouts
import org.kde.kirigami as Kirigami

Kirigami.FormLayout {
    id: root
    twinFormLayouts: parentLayout

    property var configDialog
    property var wallpaperConfiguration: wallpaper.configuration

    property string cfg_Preset: "classic"
    property alias cfg_FlameSpeed: speedSlider.value
    property alias cfg_SparkRate: sparksSlider.value
    property string cfg_FlameSpread: "auto"

    QQC2.ComboBox {
        id: presetCombo
        Kirigami.FormData.label: "Preset de Color:"
        textRole: "text"
        model: [
            { text: "🔥 Clásico (Ámbar / Rojo)", value: "classic" },
            { text: "❄️ Azul Hielo", value: "blue" },
            { text: "🌲 Esmeralda", value: "emerald" },
            { text: "🔮 Púrpura", value: "purple" },
            { text: "❄️ Cian / Escarcha", value: "cyan" },
            { text: "🧪 Tóxico (Verde Lima)", value: "toxic" }
        ]
        onActivated: root.cfg_Preset = model[currentIndex].value

        Component.onCompleted: {
            for (let i = 0; i < model.length; i++) {
                if (model[i].value === root.cfg_Preset) {
                    presetCombo.currentIndex = i;
                    break;
                }
            }
        }
    }

    RowLayout {
        Kirigami.FormData.label: "Velocidad de Llama:"
        QQC2.Slider {
            id: speedSlider
            from: 30
            to: 250
            stepSize: 5
        }
        QQC2.Label {
            text: Math.round(speedSlider.value) + "%"
        }
    }

    RowLayout {
        Kirigami.FormData.label: "Cantidad de Chispas:"
        QQC2.Slider {
            id: sparksSlider
            from: 0
            to: 250
            stepSize: 5
        }
        QQC2.Label {
            text: Math.round(sparksSlider.value) + "%"
        }
    }

    QQC2.ComboBox {
        id: spreadCombo
        Kirigami.FormData.label: "Distribución de Llamas:"
        textRole: "text"
        model: [
            { text: "Auto (Continuo para Doble Pantalla / Ultrawide)", value: "auto" },
            { text: "5 Columnas (Panorámico)", value: "5col" },
            { text: "7 Columnas (Extra Ancho Continuo)", value: "7col" },
            { text: "3 Columnas (Centrado Clásico)", value: "3col" }
        ]
        onActivated: root.cfg_FlameSpread = model[currentIndex].value

        Component.onCompleted: {
            for (let i = 0; i < model.length; i++) {
                if (model[i].value === root.cfg_FlameSpread) {
                    spreadCombo.currentIndex = i;
                    break;
                }
            }
        }
    }
}
