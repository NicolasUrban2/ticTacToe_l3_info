package controller;

import javafx.animation.Animation;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import main.Main;

import java.net.URL;
import java.util.ResourceBundle;

public class TutorialScreenLayoutController implements Initializable {

    @FXML
    private ImageView arrowImageView;
    private MainController mainController = MainController.getInstance();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image image = new Image(Main.class.getResource("/images/arrow.png").toString());
        arrowImageView.setImage(image);
        ArrowAnimationSetup();
    }

    @FXML
    private void onRetourButtonClicked() {
        mainController.changeView("welcomeScreenLayout");
    }

    private void ArrowAnimationSetup() {
        int longueurAnimation = 10;

        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.75), arrowImageView);
        transition.setFromY(arrowImageView.getY());
        transition.setToY(arrowImageView.getY()+longueurAnimation);
        transition.setCycleCount(Animation.INDEFINITE);
        transition.setAutoReverse(true);

        transition.play();
    }

}
