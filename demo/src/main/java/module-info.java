module com.example {
    requires transitive javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.example to javafx.fxml;
    exports com.example;
}