package controller;

import javafx.animation.Animation;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import main.Main;

import java.net.URL;
import java.util.ResourceBundle;

public class TutorialScreenLayoutController implements Initializable, CanSetDarkmode {

    @FXML
    private ImageView arrowImageView;
    @FXML
    private AnchorPane backgroundAnchorPane;
    @FXML
    private AnchorPane secondaryBackgroundAnchorPane;
    @FXML
    private ScrollPane backgroundScrollPane;
    @FXML
    private Label titleLabel;
    @FXML
    private Label tutorialLabel1;
    @FXML
    private Label tutorialLabel2;
    @FXML
    private Label tutorialLabel3;
    @FXML
    private Label tutorialLabel4;
    @FXML
    private GridPane gridPane1;
    @FXML
    private GridPane gridPane2;
    @FXML
    private GridPane gridPane3;
    @FXML
    private GridPane gridPane4;


    private MainController mainController = MainController.getInstance();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mainController.registerAsDarkModeObserver(this);
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

    @Override
    public void setDarkMode(boolean applyDarkMode) {
        if(applyDarkMode) {
            backgroundAnchorPane.setStyle(mainController.getDarkStyle1());
            secondaryBackgroundAnchorPane.setStyle(mainController.getDarkStyle2());
            backgroundScrollPane.setStyle(mainController.getDarkStyle2());
            titleLabel.setTextFill(Color.WHITE);
            tutorialLabel1.setTextFill(Color.WHITE);
            tutorialLabel2.setTextFill(Color.WHITE);
            tutorialLabel3.setTextFill(Color.WHITE);
            tutorialLabel4.setTextFill(Color.WHITE);
            gridPane1.setStyle("-fx-grid-lines-color: white;");

        } else {
            backgroundAnchorPane.setStyle(mainController.getBrightStyle1());
            secondaryBackgroundAnchorPane.setStyle(mainController.getBrightStyle1());
            backgroundScrollPane.setStyle(mainController.getBrightStyle2());
            titleLabel.setTextFill(Color.BLACK);
            tutorialLabel1.setTextFill(Color.BLACK);
            tutorialLabel2.setTextFill(Color.BLACK);
            tutorialLabel3.setTextFill(Color.BLACK);
            tutorialLabel4.setTextFill(Color.BLACK);
            gridPane1.setStyle("-fx-grid-lines-color: black;");
        }
    }
}
