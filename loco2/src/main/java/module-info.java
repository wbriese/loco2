module wbriese.loco2 {
    requires javafx.controls;
    exports wbriese.loco2;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.graphics;
    requires java.logging;
    requires java.base;
    requires json.simple;

    opens wbriese.loco2 to javafx.graphics, javafx.fxml, javafx.base; 
}
