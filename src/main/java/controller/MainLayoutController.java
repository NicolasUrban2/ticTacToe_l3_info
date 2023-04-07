package controller;

import javafx.animation.FadeTransition;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import main.Main;
import model.ViewAndController;

import javax.swing.text.View;
import java.awt.*;
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
    private Object mainViewController;

    private MainController mainController = MainController.getInstance();
    private final String darkModeToggleButtonSelectedStyle =    "-fx-background-radius : 20; -fx-background-color: black; -fx-border-color :  black; -fx-border-radius : 20; -fx-text-fill : white";
    private final String darkModeToggleButtonUnselectedStyle =  "-fx-background-radius : 20; -fx-background-color:  white; -fx-border-color :  black; -fx-border-radius : 20; -fx-text-fill : black";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setDarkModeToggleButtonStyle();
        mainController.registerAsDarkModeObserver(this);
        Tooltip tooltipDarkModeToggleButton = new Tooltip("Alterner entre le thème sombre et le thème clair");
        Tooltip.install(darkModeToggleButton, tooltipDarkModeToggleButton);
        changeView("welcomeScreenLayout");
    }

    @FXML
    private void onAiParametresButtonClick() throws IOException {
        openModalWindow("Paramètres", "aiSettingsOverview");
    }

    @FXML
    private void onAiModelesButtonClick() throws IOException {
        openModalWindow("Modèles", "aiModelsOverview");
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
        openWindow("Comment jouer", "tutorialScreenLayoutSmall");
    }

    @FXML
    private void onNousButtonClick() throws IOException {
        openModalWindow("A propos de nous", "nousOverview");
    }

    private void openWindow(String windowName, String fxmlFileName) throws IOException {
        ViewAndController viewAndController = ViewLoader.getView(fxmlFileName);
        Object controller = viewAndController.controller;
        Scene scene = new Scene((AnchorPane) viewAndController.node);
        Stage stage = new Stage();
        stage.setAlwaysOnTop(true);
        stage.setTitle(windowName);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                if(controller != null) {
                    if(controller instanceof CanSetDarkmode) {
                        mainController.removeFromDarkModeObservers((CanSetDarkmode) controller);
                    }
                }
            }
        });
    }

    // Permet d'ouvrir une fenêtre en rendant impossible l'interaction avec la fenêtre principale
    private void openModalWindow(String windowName, String fxmlFileName) throws IOException {
        ViewAndController viewAndController = ViewLoader.getView(fxmlFileName);
        Object controller = viewAndController.controller;
        Scene scene = new Scene((AnchorPane) viewAndController.node);
        Stage stage = new Stage();
        stage.setAlwaysOnTop(true);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(windowName);
        stage.setScene(scene);
        stage.setResizable(false);

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                enableMainWindow();
                if(controller != null) {
                    if(controller instanceof CanSetDarkmode) {
                        mainController.removeFromDarkModeObservers((CanSetDarkmode) controller);
                    }
                }
            }
        });

        stage.show();
        mainController.getRootAnchorPane().getStyle();
        disableMainWindow();
    }

    public void disableMainWindow(){
        mainController.getRootAnchorPane().setStyle(mainController.getDarkStyle1());
    }

    public void enableMainWindow(){
        mainController.getRootAnchorPane().setStyle(mainController.getRootDefaultStyle());
    }

    public void changeView(String viewName) {
        if (mainView == null) {
            // Premier affichage
            try {
                ViewAndController viewAndController = ViewLoader.getView(viewName);
                mainViewController = viewAndController.controller;
                mainView = viewAndController.node;

                mainPane.setCenter(mainView);
            } catch (Exception e) {
                System.err.println("ChangeView Error1 :" + e.getMessage());
            }
        } else {
            // Tous les affichages suivants
            FadeTransition fadeTransitionOut = new FadeTransition(Duration.millis(200), mainView);
            fadeTransitionOut.setFromValue(1);
            fadeTransitionOut.setToValue(0);
            fadeTransitionOut.play();
            fadeTransitionOut.setOnFinished(actionEvent -> {
                try {
                    if(mainViewController != null) {
                        if(mainViewController instanceof CanSetDarkmode) {
                            mainController.removeFromDarkModeObservers((CanSetDarkmode) mainViewController);
                        }
                    }

                    ViewAndController viewAndController = ViewLoader.getView(viewName);
                    mainViewController = viewAndController.controller;
                    mainView = viewAndController.node;
                    FadeTransition fadeTransitionIn = new FadeTransition(Duration.millis(200), mainView);
                    fadeTransitionIn.setFromValue(0);
                    fadeTransitionIn.setToValue(1);
                    fadeTransitionIn.play();

                    mainPane.setCenter(mainView);
                } catch (Exception e) {
                    System.err.println("ChangeView Error2 :" + e.getMessage());
                }
            });
        }
    }

    @FXML
    private void onDarkModeToggleButtonClick() {
        setDarkModeToggleButtonStyle();
        mainController.setDarkModeToggleButtonSelected(darkModeToggleButton.isSelected());
    }


    // Changer le style du bouton (Soleil ou Lune)
    private void setDarkModeToggleButtonStyle() {
        String imageName;
        if(darkModeToggleButton.isSelected()) {
            imageName = "sunIcon.png";
            darkModeToggleButton.setStyle(darkModeToggleButtonSelectedStyle);
        } else {
            darkModeToggleButton.setStyle(darkModeToggleButtonUnselectedStyle);
            imageName = "moonIcon.png";
        }
        ImageView newIcon = new ImageView(new Image(Main.class.getResource("/images/"+imageName).toString()));
        newIcon.setPreserveRatio(true);
        newIcon.setFitWidth(15);
        darkModeToggleButton.setGraphic(newIcon);
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
