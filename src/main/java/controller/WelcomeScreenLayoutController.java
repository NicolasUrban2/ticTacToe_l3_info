package controller;

import javafx.animation.Animation;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import main.Main;
import model.GameSettings;

import java.net.URL;
import java.util.ResourceBundle;

public class WelcomeScreenLayoutController implements Initializable {
    @FXML
    private ImageView imageLoutre;

    @FXML
    private Button humanVsAiButton;
    @FXML
    private Button humanVsHumanButton;
    @FXML
    private Button tutorialButton;

    private GameSettings gameSettings = GameSettings.getInstance();

    private MainController mainController = MainController.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loutreAnimationSetup();
        Tooltip tooltipHumanVsAiButton = new Tooltip("Jouer contre une intelligence artificielle");
        Tooltip tooltipHumanVsAiHuman = new Tooltip("Jouer contre un autre joueur sur le même écran");
        Tooltip tooltipTutorialButton = new Tooltip("Voir comment jouer au jeu du Tic Tac Toe");
        Tooltip.install(humanVsAiButton, tooltipHumanVsAiButton);
        Tooltip.install(humanVsHumanButton, tooltipHumanVsAiHuman);
        Tooltip.install(tutorialButton, tooltipTutorialButton);

    }

    private void loutreAnimationSetup() {
        int longueurAnimation = 10;

        TranslateTransition transition = new TranslateTransition(Duration.seconds(2), imageLoutre);
        transition.setFromY(imageLoutre.getY());
        transition.setToY(imageLoutre.getY()+longueurAnimation);
        transition.setCycleCount(Animation.INDEFINITE);
        transition.setAutoReverse(true);

        transition.play();
    }

    @FXML
    public void onJoueurVsIAButtonClick() {
        System.out.println("Affichage du choix des difficultés.");
        gameSettings.setGameMode("pve");
        mainController.changeView("difficultyChoiceOverview");
    }

    @FXML
    public void onJoueurVsJoueurButtonClick() {
        System.out.println("Affichage JVJ");
        gameSettings.setGameMode("pvp");
        mainController.changeView("gameScreenLayout");
    }

    @FXML
    private void onTutorialButtonClick() {
        mainController.changeView("tutorialScreenLayout");
    }
}
