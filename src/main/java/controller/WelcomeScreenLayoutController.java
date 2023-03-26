package controller;

import javafx.animation.Animation;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import main.Main;

import java.net.URL;
import java.util.ResourceBundle;

public class WelcomeScreenLayoutController implements Initializable {
    @FXML
    private ImageView imageLoutre;

    private MainController mainController = MainController.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loutreAnimationSetup();
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
        mainController.changeView("difficultyChoiceOverview");
    }
}
