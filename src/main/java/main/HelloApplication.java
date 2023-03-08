package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.*;

public class HelloApplication extends Application {

    public static void main(String[] args) {
        launch();
    }
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(HelloApplication.class.getResource("/aiLearningOverview.fxml"));
        AnchorPane aiLearningOverview = (AnchorPane) loader.load();
        Scene scene = new Scene(aiLearningOverview);
        stage.setTitle("Tic Tac Toe");
        stage.setScene(scene);
        stage.show();
    }
}