package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.HelloApplication;

import java.io.IOException;

public class MainLayoutController{
    @FXML
    private MenuItem parametresMenuItem;

    @FXML
    public void onAiParametresButtonClick() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(HelloApplication.class.getResource("/aiSettingsOverview.fxml"));
        AnchorPane aiSettingsOverview = (AnchorPane) loader.load();
        Scene scene = new Scene(aiSettingsOverview);
        Stage stage = new Stage();
        stage.setAlwaysOnTop(true);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Paramètres");
        stage.setScene(scene);

        stage.setOnCloseRequest(event -> {
            enableMainWindow();
        });

        stage.show();
        HelloApplication.rootAnchorPane.getStyle();
        disableMainWindow();
    }

    public static void disableMainWindow(){
        Stage root = main.HelloApplication.root;
        root.setResizable(false);
        HelloApplication.rootAnchorPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5)");
    }

    public static void enableMainWindow(){
        Stage root = HelloApplication.root;
        root.setResizable(true);
        HelloApplication.rootAnchorPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0)");
    }
}
