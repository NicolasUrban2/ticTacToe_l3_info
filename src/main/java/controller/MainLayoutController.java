package controller;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.Main;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;

public class MainLayoutController implements Initializable, CanSetDarkmode {
    @FXML
    private ToggleButton darkModeToggleButton;
    @FXML
    private BorderPane mainPane;

    private Node mainView;

    private MainController mainController = MainController.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mainController.registerAsDarkModeObserver(this);
        Tooltip tooltipDarkModeToggleButton = new Tooltip("Alterner entre le thème sombre et le thème clair");
        Tooltip.install(darkModeToggleButton, tooltipDarkModeToggleButton);
        changeView("welcomeScreenLayout");
    }

    @FXML
    private void onDarkModeToggleButtonClick() {
        mainController.setDarkModeToggleButtonSelected(darkModeToggleButton.isSelected());
    }

    @FXML
    private void onAiParametresButtonClick() throws IOException {
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
    private void onAiModelesButtonClick() throws IOException {
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

    @FXML
    private void onGithubButtonClick() {
        String githubUrl = "https://github.com/NicolasUrban2/ticTacToe_l3_info.git";
        try {
            java.awt.Desktop.getDesktop().browse(new URI(githubUrl));
        } catch (Exception e) {
            System.err.println("Github link error : " + e.getMessage());
        }
    }

    @FXML
    private void onWikipediaButtonClick() {
        String wikipediaUrl = "https://fr.wikipedia.org/wiki/Tic-tac-toe";
        try {
            java.awt.Desktop.getDesktop().browse(new URI(wikipediaUrl));
        } catch (Exception e) {
            System.err.println("Wikipedia link error : " + e.getMessage());
        }
    }

    @FXML
    private void onCommentJouerButtonClick() throws IOException {
        Scene scene = new Scene((AnchorPane) ViewLoader.getView("tutorialScreenLayoutSmall"));
        Stage stage = new Stage();
        stage.setAlwaysOnTop(true);
        stage.setTitle("Comment jouer");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    private void onNousButtonClick() throws IOException {
        Scene scene = new Scene((AnchorPane) ViewLoader.getView("nousOverview"));
        Stage stage = new Stage();
        stage.setAlwaysOnTop(true);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Nous");
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
        mainController.getRootAnchorPane().setStyle(mainController.getDarkStyle1());
    }

    public void enableMainWindow(){
        Stage root = mainController.getRoot();
        root.setResizable(true);
        mainController.getRootAnchorPane().setStyle(mainController.getRootDefaultStyle());
    }

    public void changeView(String viewName) {
        //System.out.println(viewName);
        if (mainView != null) {
            FadeTransition fadeTransitionOut = new FadeTransition(Duration.millis(200), mainView);
            fadeTransitionOut.setFromValue(1);
            fadeTransitionOut.setToValue(0);
            fadeTransitionOut.play();
            fadeTransitionOut.setOnFinished(actionEvent -> {
                try {
                    mainView = ViewLoader.getView(viewName);
                    //mainController.setDarkModeToAllObservers(darkModeToggleButton.isSelected());
                    FadeTransition fadeTransitionIn = new FadeTransition(Duration.millis(200), mainView);
                    fadeTransitionIn.setFromValue(0);
                    fadeTransitionIn.setToValue(1);
                    fadeTransitionIn.play();
                    mainPane.setCenter(mainView);
                } catch (Exception e) {
                    System.err.println("ChangeViewError :" + e.getMessage());
                }
            });
        } else {
            try {
                mainView = ViewLoader.getView(viewName);
                //mainController.setDarkModeToAllObservers(darkModeToggleButton.isSelected());
                mainPane.setCenter(mainView);
            } catch (Exception e) {
                System.err.println("ChangeViewError :" + e.getMessage());
            }
        }
    }

    @Override
    public void setDarkMode(boolean applyDarkMode) {
        if(applyDarkMode) {
            mainPane.setStyle(mainController.getDarkStyle3());
        } else {
            mainPane.setStyle(mainController.getBrightStyle1());
        }
    }
}
