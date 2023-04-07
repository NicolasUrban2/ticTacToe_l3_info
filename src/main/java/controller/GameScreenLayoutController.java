package controller;

import ai.Coup;
import ai.MultiLayerPerceptron;
import javafx.animation.FadeTransition;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import main.Main;
import model.*;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class GameScreenLayoutController implements Initializable, CanSetDarkmode {
    @FXML
    private Label tourJ1LabelGauche;
    @FXML
    private Label tourJ2LabelDroite;
    @FXML
    private AnchorPane backgroundAnchorPane;
    @FXML
    private Button accueilButton;
    @FXML
    private Label winOrLose;
    @FXML
    private Label player1Label;
    @FXML
    private Label player2Label;
    @FXML
    private Label titleLabel;
    @FXML
    private GridPane gridPane;
    @FXML
    private Button replayButton;
    @FXML
    private CheckBox playerOnePlaysFirstCheckbox;

    private ImageView[][] imageViewCircleTable = new ImageView[3][3];

    private ImageView[][] imageViewCrossTable = new ImageView[3][3];

    private ImageView[][] imageViewEmptyTable = new ImageView[3][3];

    private boolean playerRound = true; // True = Player 1 (Left)
                                        // False = Player 2 (Right)

    private GameSettings gameSettings = GameSettings.getInstance();
    private GameManager gameManager = GameManager.getInstance();
    private MainController mainController = MainController.getInstance();

    private boolean isGamePlayable;
    private boolean isGameFinished;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        mainController.registerAsDarkModeObserver(this);

        gameManager.emptyCoup();

        replayButtonInitialization();
        accueilButtonInitialization();

        isGameFinished = false;
        isGamePlayable = true;
        playerRound = true;

        winOrLose.setText("");
        switch (gameSettings.getGameMode()) {
            case "pvp" :
                titleLabel.setText("Mode de jeu : Joueur contre joueur");
                playerOnePlaysFirstCheckbox.setVisible(false);
                break;
            case "pve" :
                titleLabel.setText("Mode de jeu : Joueur contre IA");
                player1Label.setText("Vous");
                player2Label.setText("IA");
                playerOnePlaysFirstCheckbox.setVisible(true);
                break;
            default:
                titleLabel.setText("Mode de jeu : Erreur");
                playerOnePlaysFirstCheckbox.setVisible(false);
        }

        gridPane.getChildren().clear();
        fillEmptyImagesTable();
        fillCircleTable();
        fillCrossTable();
        removeHighlightCases();

        tourJ1LabelGauche.setVisible(true);
        tourJ2LabelDroite.setVisible(false);

        if(!playerOnePlaysFirstCheckbox.isSelected() && gameSettings.getGameMode().equals("pve")) {
            makeAiToPlay();
        }
    }

    private void removeHighlightCases() {
        for(int i=0; i<3; i++) {
            highlightCases(i, 0, i, 1, i, 2, false);
        }
    }

    private void replayButtonInitialization() {
        ImageView imageView = new ImageView(new Image(Main.class.getResource("/images/replayIcon.png").toString()));
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(20);
        replayButton.setGraphic(imageView);
    }

    private void accueilButtonInitialization() {
        ImageView house = new ImageView(Main.class.getResource("/images/house.png").toString());
        accueilButton.setGraphic(house);
        house.setFitHeight(20);
        house.setFitWidth(20);
    }

    private void fillEmptyImagesTable() {
        for (int i=0; i<3; i++) {
            for (int j=0; j<3; j++) {
                ImageView image = new ImageView(new Image(Main.class.getResource("/images/white.jpg").toString()));
                image.setFitHeight(90);
                image.setFitWidth(90);
                image.setPreserveRatio(true);
                image.setCursor(Cursor.HAND);
                imageViewEmptyTable[i][j] = image;
                gridPane.add(imageViewEmptyTable[i][j], i , j);
                Insets insets = new Insets(5);
                gridPane.setMargin(image, insets);
                setClickListenerOnImage(image);
            }
        }
    }

    private void setClickListenerOnImage(ImageView image) {
        image.setOnMouseClicked(event -> {
            if(isGamePlayable) {
                makeHumanToPlay(image);
            }

            // Changement de tour ou tour de l'IA
            if(gameSettings.getGameMode().equals("pve")) {
                tourJ1LabelGauche.setVisible(false);
                if(isGamePlayable) {
                    makeAiToPlay();
                }
            } else if(!isGameFinished) {
                changeTurn();
            } else {
                tourJ1LabelGauche.setVisible(false);
                tourJ2LabelDroite.setVisible(false);
            }
        });
    }

    private void changeTurn() {
        if(playerRound) {
            tourJ1LabelGauche.setVisible(false);
            tourJ2LabelDroite.setVisible(true);
        } else {
            tourJ1LabelGauche.setVisible(true);
            tourJ2LabelDroite.setVisible(false);
        }
        playerRound = ! playerRound;
    }

    private void makeHumanToPlay(ImageView image) {
        int xClickedCase = GridPane.getColumnIndex(image);
        int yClickedCase = GridPane.getRowIndex(image);

        if(playerRound) {
            displayMove(imageViewCrossTable[xClickedCase][yClickedCase]);
            gameManager.addMoveToCoup(xClickedCase, yClickedCase, -1);
        }
        else {
            displayMove(imageViewCircleTable[xClickedCase][yClickedCase]);
            gameManager.addMoveToCoup(xClickedCase, yClickedCase, 1);
        }
        //System.out.println(gameManager.getCoup());
        turnConclusion(xClickedCase, yClickedCase);
    }

    private static void displayMove(ImageView imageView) {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(200), imageView);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();
        imageView.setVisible(true);
    }

    private void makeAiToPlay() {
        MoveCoordinates moveCoordinates = AiPlayer.getInstance(gameSettings.getMultiLayerPerceptron()).getNextMoveCoordinates(gameManager.getCoup());
        displayAiMove(moveCoordinates.x, moveCoordinates.y);

        gameManager.addMoveToCoup(moveCoordinates.x, moveCoordinates.y, 1);
        turnConclusion(moveCoordinates.x, moveCoordinates.y);
    }

    private void displayAiMove(int xAiMoveCoordinates, int yAiMoveCoordinates) {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                tourJ1LabelGauche.setVisible(false);
                tourJ2LabelDroite.setVisible(true);
                isGamePlayable = false;
                Thread.sleep(500);
                displayMove(imageViewCircleTable[xAiMoveCoordinates][yAiMoveCoordinates]);
                isGamePlayable = !isGameFinished;
                tourJ1LabelGauche.setVisible(true);
                tourJ2LabelDroite.setVisible(false);
                return null;
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    private void fillCircleTable() {
        for (int i=0; i<3; i++) {
            for (int j=0; j<3; j++) {
                ImageView image = new ImageView(new Image(Main.class.getResource("/images/orange_circle.jpg").toString()));
                image.setFitHeight(90.0);
                image.setFitWidth(90.0);
                image.setPreserveRatio(true);
                image.setVisible(false);
                Insets insets = new Insets(5);
                gridPane.setMargin(image, insets);
                imageViewCircleTable[i][j] = image;
                gridPane.add(imageViewCircleTable[i][j], i , j);
            }
        }
    }

    private void fillCrossTable() {
        for (int i=0; i<3; i++) {
            for (int j=0; j<3; j++) {
                ImageView image = new ImageView(new Image(Main.class.getResource("/images/blue_cross.jpg").toString()));
                image.setFitHeight(90.0);
                image.setFitWidth(90.0);
                image.setPreserveRatio(true);
                image.setVisible(false);
                Insets insets = new Insets(5);
                gridPane.setMargin(image, insets);
                imageViewCrossTable[i][j] = image;
                gridPane.add(imageViewCrossTable[i][j], i , j);
            }
        }
    }

    private void turnConclusion(int x, int y) {
        WinningMove winningMove = gameManager.gameFinished(x, y);

        if(winningMove != null) {
            //System.out.println("Fin");
            //System.out.println(winningMove.victoryToken);
            if(winningMove.isDraw) {
                winOrLose.setText("Match nul");
                isGamePlayable = false;
                isGameFinished = true;
            } else if(winningMove.victoryToken == 1) {
                highlightCases(winningMove.xA, winningMove.yA, winningMove.xB, winningMove.yB, winningMove.xC, winningMove.yC, true);
                if(gameSettings.getGameMode().equals("pve")) {
                    winOrLose.setText("Victoire !");
                } else {
                    winOrLose.setText("Victoire du joueur 1 !");
                }
                isGamePlayable = false;
                isGameFinished = true;
            } else if(winningMove.victoryToken == 2) {
                highlightCases(winningMove.xA, winningMove.yA, winningMove.xB, winningMove.yB, winningMove.xC, winningMove.yC, true);
                if(gameSettings.getGameMode().equals("pve")) {
                    winOrLose.setText("Défaite...");
                } else {
                    winOrLose.setText("Victoire du joueur 2 !");
                }
                isGamePlayable = false;
                isGameFinished = true;
            } else {
                winOrLose.setText("Erreur...");
                isGamePlayable = false;
                isGameFinished = true;
            }
        } else {
            // System.out.println("Continue");
            winOrLose.setText("");
        }
    }

    @FXML
    private void onAccueilButtonClick() {
        mainController.changeView("welcomeScreenLayout");
    }
    @FXML
    private void onReplayButtonClick() {
        mainController.removeFromDarkModeObservers(this);
        initialize(null, null);
    }
    @FXML
    private void setPlayerOnePlaysFirstCheckboxClick() {
        onReplayButtonClick();
    }

    private void highlightCases(int xA, int yA, int xB, int yB, int xC, int yC, boolean apply) {
        Effect effect;
        if(apply) {
            effect = new Bloom();
        } else {
            effect = null;
        }
        imageViewCircleTable[xA][yA].setEffect(effect);
        imageViewCircleTable[xB][yB].setEffect(effect);
        imageViewCircleTable[xC][yC].setEffect(effect);

        imageViewCrossTable[xA][yA].setEffect(effect);
        imageViewCrossTable[xB][yB].setEffect(effect);
        imageViewCrossTable[xC][yC].setEffect(effect);
    }

    @Override
    public void setDarkMode(boolean applyDarkMode) {
        if(applyDarkMode) {
            backgroundAnchorPane.setStyle(mainController.getDarkStyle1());
            titleLabel.setTextFill(Color.WHITE);
            tourJ1LabelGauche.setTextFill(Color.WHITE);
            tourJ2LabelDroite.setTextFill(Color.WHITE);
            winOrLose.setTextFill(Color.WHITE);
            player1Label.setTextFill(Color.WHITE);
            player2Label.setTextFill(Color.WHITE);
            playerOnePlaysFirstCheckbox.setTextFill(Color.WHITE);
        } else {
            backgroundAnchorPane.setStyle(mainController.getBrightStyle1());
            titleLabel.setTextFill(Color.BLACK);
            tourJ1LabelGauche.setTextFill(Color.BLACK);
            tourJ2LabelDroite.setTextFill(Color.BLACK);
            winOrLose.setTextFill(Color.BLACK);
            player1Label.setTextFill(Color.BLACK);
            player2Label.setTextFill(Color.BLACK);
            playerOnePlaysFirstCheckbox.setTextFill(Color.BLACK);
        }
    }
}
