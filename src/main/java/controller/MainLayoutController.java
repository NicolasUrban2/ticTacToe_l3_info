package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
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
        stage.setTitle("Paramètres");
        stage.setScene(scene);
        stage.show();
    }
}
