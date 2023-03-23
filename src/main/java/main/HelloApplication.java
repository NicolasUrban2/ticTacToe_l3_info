package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.*;

public class HelloApplication extends Application {

    public static Stage root;
    public static AnchorPane rootAnchorPane;
    public static void main(String[] args) {
        launch();
    }
    @Override
    public void start(Stage stage) throws IOException {
        root = stage;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(HelloApplication.class.getResource("/mainLayout.fxml"));
        this.rootAnchorPane = (AnchorPane) loader.load();
        Scene scene = new Scene(rootAnchorPane);
        root.setTitle("Tic Tac Toe");
        root.setScene(scene);
        root.show();
    }
}