module main {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    exports main;
    opens main to javafx.fxml;
    exports controller;
    opens controller to javafx.fxml;
}