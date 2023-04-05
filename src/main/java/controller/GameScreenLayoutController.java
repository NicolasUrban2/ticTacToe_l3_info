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
import model.GameSettings;

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

    private double[] in = new double[9];

    private Coup coup = new Coup(9,"");

    private GameSettings gameSettings = GameSettings.getInstance();

    private MainController mainController = MainController.getInstance();

    private boolean isGamePlayable;
    private boolean isGameFinished;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        replayButtonInitialization();

        mainController.registerAsDarkModeObserver(this);
        isGameFinished = false;
        accueilButtonInitialization();
        winOrLose.setText("");
        Arrays.fill(in, 0);

        switch (gameSettings.getGameMode()) {
            case "pvp" :
                titleLabel.setText("Mode de jeu : Joueur contre joueur");
                playerOnePlaysFirstCheckbox.setVisible(false);
                break;
            case "pve" :
                titleLabel.setText("Mode de jeu : Joueur contre IA");
                playerOnePlaysFirstCheckbox.setVisible(true);
                break;
            default:
                titleLabel.setText("Mode de jeu : Erreur");
                playerOnePlaysFirstCheckbox.setVisible(false);
        }

        tourJ1LabelGauche.setVisible(true);
        tourJ2LabelDroite.setVisible(false);

        if(gameSettings.getGameMode() == "pve") {
            player1Label.setText("Vous");
            player2Label.setText("IA");
        }

        isGamePlayable = true;
        playerRound = true;
        //replayButton.setText("Recommencer");
        gridPane.getChildren().clear();
        fillEmptyImagesTable();
        fillCircleTable();
        fillCrossTable();

        for(int i=0; i<7; i+=3) {
            highlightCases(i, i+1, i+2, false);
        }

        if(!playerOnePlaysFirstCheckbox.isSelected() && gameSettings.getGameMode().equals("pve")) {
            makeAiToPlay();
        }
    }

    @FXML
    private void setPlayerOnePlaysFirstCheckboxClick() {
        initialize(null, null);
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
            playerRound = false;
        } else {
            tourJ1LabelGauche.setVisible(true);
            tourJ2LabelDroite.setVisible(false);
            playerRound = true;
        }
    }

    private void makeHumanToPlay(ImageView image) {
        int xClickedCase = GridPane.getColumnIndex(image);
        int yClickedCase = GridPane.getRowIndex(image);

        if(playerRound) {
            FadeTransition fadeTransition = new FadeTransition(Duration.millis(200), imageViewCrossTable[xClickedCase][yClickedCase]);
            fadeTransition.setFromValue(0);
            fadeTransition.setToValue(1);
            fadeTransition.play();
            imageViewCrossTable[xClickedCase][yClickedCase].setVisible(true);
            in[xClickedCase + yClickedCase *3] = -1;
        }
        else {
            FadeTransition fadeTransition = new FadeTransition(Duration.millis(200), imageViewCircleTable[xClickedCase][yClickedCase]);
            fadeTransition.setFromValue(0);
            fadeTransition.setToValue(1);
            fadeTransition.play();
            imageViewCircleTable[xClickedCase][yClickedCase].setVisible(true);
            in[xClickedCase + yClickedCase *3] = 1;
        }
        coup.addInBoard(in);
        turnConclusion(xClickedCase, yClickedCase);
    }

    private void makeAiToPlay() {
        System.out.println("Ai Turn");
        int index = getNextMoveIndex(getAiMoveTable());
        int xAiMoveCoordinates, yAiMoveCoordinates;
        if(index < 3) {
            xAiMoveCoordinates = index;
            yAiMoveCoordinates = 0;
        } else if(index < 6) {
            xAiMoveCoordinates = index-3;
            yAiMoveCoordinates = 1;
        } else {
            xAiMoveCoordinates = index-6;
            yAiMoveCoordinates = 2;
        }
        displayAiMove(xAiMoveCoordinates, yAiMoveCoordinates);

        in[index] = 1;
        coup.addInBoard(in);
        turnConclusion(xAiMoveCoordinates, yAiMoveCoordinates);
    }

    private void displayAiMove(int xAiMoveCoordinates, int yAiMoveCoordinates) {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                tourJ1LabelGauche.setVisible(false);
                tourJ2LabelDroite.setVisible(true);
                isGamePlayable = false;
                Thread.sleep(500);
                FadeTransition fadeTransition = new FadeTransition(Duration.millis(200), imageViewCircleTable[xAiMoveCoordinates][yAiMoveCoordinates]);
                fadeTransition.setFromValue(0);
                fadeTransition.setToValue(1);
                fadeTransition.play();
                imageViewCircleTable[xAiMoveCoordinates][yAiMoveCoordinates].setVisible(true);
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
        switch (gameFinished(x, y)) {
            case 0:
                winOrLose.setText("Match nul");
                //replayButton.setText("Rejouer");
                isGamePlayable = false;
                isGameFinished = true;
                break;
            case 1:
                if(gameSettings.getGameMode().equals("pve")) {
                    winOrLose.setText("Victoire !");
                } else {
                    winOrLose.setText("Victoire du joueur 1 !");
                }
                //replayButton.setText("Rejouer");
                isGamePlayable = false;
                isGameFinished = true;
                break;
            case 2:
                if(gameSettings.getGameMode().equals("pve")) {
                    winOrLose.setText("Défaite...");
                } else {
                    winOrLose.setText("Victoire du joueur 2 !");
                }
                //replayButton.setText("Rejouer");
                isGamePlayable = false;
                isGameFinished = true;
                break;
            default:
                winOrLose.setText("");
        }
    }

    private int getNextMoveIndex(double[] res) {
        int index = 0;
        double max = 0;
        for(int i=0; i<9; i++) {
            if(in[i] == 0.0 && res[i] > max) {
                max = res[i];
                index = i;
            }
        }
        return index;
    }

    private double[] getAiMoveTable() {
        MultiLayerPerceptron multiLayerPerceptron = gameSettings.getMultiLayerPerceptron();
        double[] res = multiLayerPerceptron.forwardPropagation(in);
        return res;
    }
    @FXML
    private void onAccueilButtonClick() {
        mainController.changeView("welcomeScreenLayout");
    }

    @FXML
    private void onReplayButtonClick() {
        initialize(null, null);
    }

    private int gameFinished(int x, int y) {
        int playerToken = (int) in[x+3*y];
        // Test horizontal
        boolean isWiningLine = true;
        int i=0;
        while(i<3 && isWiningLine) {
            if(in[i+3*y] != playerToken) {
                isWiningLine = false;
            }
            i++;
        }
        if(isWiningLine) {
            highlightCases(0+3*y, 1+3*y, 2+3*y, true);
            return getVictoryForPlayerToken(playerToken);
        }
        // test vertical
        isWiningLine = true;
        i=0;
        while(i<3 && isWiningLine) {
            if(in[x+3*i] != playerToken) {
                isWiningLine = false;
            }
            i++;
        }
        if(isWiningLine) {
            highlightCases(x+3*0, x+3*1, x+3*2, true);
            return getVictoryForPlayerToken(playerToken);
        }
        // Test diagonal from upper left
        isWiningLine = true;
        i=0;
        while(i<3 && isWiningLine) {
            if(in[i+3*i] != playerToken) {
                isWiningLine = false;
            }
            i++;
        }
        if(isWiningLine) {
            highlightCases(0+3*0, 1+3*1, 2+3*2, true);
            return getVictoryForPlayerToken(playerToken);
        }
        // Test diagonal from upper right
        isWiningLine = true;
        i=2;
        int j=0;
        while(i>=0 && isWiningLine) {
            if(in[i+3*j] != playerToken) {
                isWiningLine = false;
            }
            i--;
            j++;
        }
        if(isWiningLine) {
            highlightCases(2+3*0, 1+3*1, 0+3*2, true);
            return getVictoryForPlayerToken(playerToken);
        }

        boolean isDraw = true;
        int t=0;
        while(t < in.length && isDraw) {
            if(in[t] == 0) {
                isDraw = false;
            }
            t++;
        }
        if(isDraw) {
            return 0;
        }
        return -1;
    }

    private int getVictoryForPlayerToken(int playerToken) {
        return switch (playerToken) {
            case -1 -> 1;
            case 1 -> 2;
            default -> -1;
        };
    }

    private void highlightCases(int a, int b, int c, boolean apply) {
        int xA, yA, xB, yB, xC, yC;
        if(a < 3) {
            xA = a;
            yA = 0;
        } else if(a < 6) {
            xA = a-3;
            yA = 1;
        } else {
            xA = a-6;
            yA = 2;
        }

        if(b < 3) {
            xB = b;
            yB = 0;
        } else if(b < 6) {
            xB = b-3;
            yB = 1;
        } else {
            xB = b-6;
            yB = 2;
        }

        if(c < 3) {
            xC = c;
            yC = 0;
        } else if(c < 6) {
            xC = c-3;
            yC = 1;
        } else {
            xC = c-6;
            yC = 2;
        }

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
