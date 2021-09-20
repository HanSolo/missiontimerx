module eu.hansolo.fx.missiontimerx {
    // Java
    requires java.base;
    requires java.net.http;
    requires java.desktop;

    // Java-FX
    requires javafx.base;
    requires javafx.graphics;
    requires javafx.controls;

    exports eu.hansolo.fx.missiontimerx;
    exports eu.hansolo.fx.missiontimerx.events;
    exports eu.hansolo.fx.missiontimerx.fonts;
}