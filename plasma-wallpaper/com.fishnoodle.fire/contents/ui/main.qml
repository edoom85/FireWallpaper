import QtQuick
import QtWebEngine
import org.kde.plasma.plasmoid

WallpaperItem {
    id: root

    WebEngineView {
        id: web
        anchors.fill: parent
        visible: true
        activeFocusOnPress: false
        url: Qt.resolvedUrl("../web/index.html")

        onJavaScriptConsoleMessage: function(level, message, lineNumber, sourceID) {
            console.error("[JS_WALLPAPER]", message);
        }

        function sendConfig() {
            if (!web.loading) {
                const preset = root.configuration.Preset || "classic";
                const speed = root.configuration.FlameSpeed || 100;
                const sparks = root.configuration.SparkRate || 100;
                const spread = root.configuration.FlameSpread || "auto";

                const script = `
                    if (window.applyPlasmaConfig) {
                        window.applyPlasmaConfig({
                            preset: "${preset}",
                            speed: ${speed},
                            sparks: ${sparks},
                            spread: "${spread}"
                        });
                    }
                `;
                web.runJavaScript(script);
            }
        }

        onLoadingChanged: function(loadRequest) {
            if (loadRequest.status === WebEngineView.LoadSucceededStatus) {
                sendConfig();
            }
        }

        Connections {
            target: root.configuration
            function onPresetChanged() { web.sendConfig(); }
            function onFlameSpeedChanged() { web.sendConfig(); }
            function onSparkRateChanged() { web.sendConfig(); }
            function onFlameSpreadChanged() { web.sendConfig(); }
        }
    }
}
