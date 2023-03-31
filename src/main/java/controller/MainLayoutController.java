package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.Main;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainLayoutController implements Initializable {
    @FXML
    private MenuItem parametresMenuItem;

    @FXML
    private BorderPane mainPane;

    private MainController mainController = MainController.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        changeView("welcomeScreenLayout");
    }

    @FXML
    public void onAiParametresButtonClick() throws IOException {
        Scene scene = new Scene((AnchorPane) ViewLoader.getView("aiSettingsOverview"));
        Stage stage = new Stage();
        stage.setAlwaysOnTop(true);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Paramètres");
        stage.setScene(scene);
        stage.setResizable(false);

        stage.setOnCloseRequest(event -> {
            enableMainWindow();
        });

        stage.show();
        mainController.getRootAnchorPane().getStyle();
        disableMainWindow();
    }

    @FXML
    public void onAiModelesButtonClick() throws IOException {
        Scene scene = new Scene((AnchorPane) ViewLoader.getView("aiModelsOverview"));
        Stage stage = new Stage();
        stage.setAlwaysOnTop(true);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Modèles");
        stage.setScene(scene);
        stage.setResizable(false);

        stage.setOnCloseRequest(event -> {
            enableMainWindow();
        });

        stage.show();
        mainController.getRootAnchorPane().getStyle();
        disableMainWindow();
    }

    public void disableMainWindow(){
        Stage root = mainController.getRoot();
        root.setResizable(false);
        mainController.getRootAnchorPane().setStyle("-fx-background-color: rgba(0, 0, 0, 0.5)");
    }

    public void enableMainWindow(){
        Stage root = mainController.getRoot();
        root.setResizable(true);
        mainController.getRootAnchorPane().setStyle("-fx-background-color: rgba(0, 0, 0, 0)");
    }

    public void changeView(String viewName) {
        try {
            System.out.println(viewName);
            mainPane.setCenter(ViewLoader.getView(viewName));
        } catch(Exception e) {
            System.err.println("ChangeViewError :" + e.getMessage());
        }
    }

}
