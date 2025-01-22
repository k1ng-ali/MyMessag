module org.example.Client.UIController {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires com.fasterxml.jackson.databind;
    requires java.desktop;


    opens org.example.shared to com.fasterxml.jackson.databind;


    opens org.example.Client.UIController to javafx.fxml;
    exports org.example.Client.UIController;
    exports org.example.Server;

    exports org.example.shared to com.fasterxml.jackson.databind;
    exports org.example.Client;
    opens org.example.Client to javafx.fxml; // Экспорт для Jackson


}