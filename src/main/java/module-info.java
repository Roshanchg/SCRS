module org.example.scrs {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;

    opens org.example.scrs to javafx.fxml;
    exports org.example.scrs;
    exports org.example.scrs.Controllers;
    exports org.example.scrs.Classes;
    opens org.example.scrs.Controllers to javafx.fxml;
}