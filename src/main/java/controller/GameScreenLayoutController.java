package controller;

import ai.Coup;
import ai.MultiLayerPerceptron;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import main.Main;
import model.GameSettings;

import java.net.URL;
import java.util.ResourceBundle;

public class GameScreenLayoutController implements Initializable {
    @FXML
    private Label tourJ1LabelGauche;

    @FXML
    private Button accueilButton;

    @FXML
    private Label tourJ2LabelDroite;

    @FXML
    private Label winOrLose;

    @FXML
    private Label titleLabel;

    @FXML
    private GridPane gridPane;

    @FXML
    private Button replayButton;

    private ImageView[][] imageViewCircleTable = new ImageView[3][3];

    private ImageView[][] imageViewCrossTable = new ImageView[3][3];

    private int xClickedCase;
    private int yClickedCase;
    private boolean playerRound = true; // True = Player 1 (Left)
                                        // False = Player 2 (Right)

    private double[] in = new double[9];

    private Coup coup = new Coup(9,"");

    private GameSettings gameSettings = GameSettings.getInstance();

    private MainController mainController = MainController.getInstance();

    private boolean isGamePlayable;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        accueilButtonInitialization();
        winOrLose.setText("");
        for(int t=0; t<in.length; t++) {
            in[t] = 0;
        }
        titleLabel.setText("Mode de jeu : "+gameSettings.getGameMode());
        tourJ1LabelGauche.setVisible(true);
        tourJ2LabelDroite.setVisible(false);
        isGamePlayable = true;
        playerRound = true;
        replayButton.setText("Recommencer");
        gridPane.getChildren().clear();
        fillEmptyImagesTable();
        fillCircleTable();
        fillCrossTable();

        for(int i=0; i<7; i+=3) {
            highlightCases(i, i+1, i+2, false);
        }
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
                image.setOpacity(100);
                gridPane.add(image, i , j);
                image.setOnMouseClicked(event -> {
                    if(isGamePlayable) {
                        xClickedCase = GridPane.getColumnIndex(image);
                        yClickedCase = GridPane.getRowIndex(image);
                        //System.out.println(xClickedCase + " " + yClickedCase);

                        if(playerRound) {
                            imageViewCrossTable[xClickedCase][yClickedCase].setVisible(true);
                            in[xClickedCase+yClickedCase*3] = -1;
                        }
                        else {
                            imageViewCircleTable[xClickedCase][yClickedCase].setVisible(true);
                            in[xClickedCase+yClickedCase*3] = 1;
                        }
                        coup.addInBoard(in);
                        //System.out.println(coup.toString());
                        turnConclusion(xClickedCase, yClickedCase);
                        // Changement de tour et tour de l'IA
                        if(playerRound) {
                            if(gameSettings.getGameMode() == "pve") {
                                if(isGamePlayable) {
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

                                    imageViewCircleTable[xAiMoveCoordinates][yAiMoveCoordinates].setVisible(true);
                                    in[index] = 1;
                                    coup.addInBoard(in);
                                    turnConclusion(xAiMoveCoordinates, yAiMoveCoordinates);
                                }
                            } else {
                                tourJ2LabelDroite.setVisible(true);
                                tourJ1LabelGauche.setVisible(false);
                                playerRound = false;
                            }
                        } else {
                            tourJ2LabelDroite.setVisible(false);
                            tourJ1LabelGauche.setVisible(true);
                            playerRound = true;
                            //System.out.println("Player Turn");
                        }
                    }

                });
            }
        }
    }

    private void turnConclusion(int x, int y) {
        switch (gameFinished(x, y)) {
            case 0:
                winOrLose.setText("Match nul");
                replayButton.setText("Rejouer");
                isGamePlayable = false;
                break;
            case 1:
                if(gameSettings.getGameMode().equals("pve")) {
                    winOrLose.setText("Victoire !");
                } else {
                    winOrLose.setText("Victoire du joueur 1 !");
                }
                replayButton.setText("Rejouer");
                isGamePlayable = false;
                break;
            case 2:
                if(gameSettings.getGameMode().equals("pve")) {
                    winOrLose.setText("Défaite...");
                } else {
                    winOrLose.setText("Victoire du joueur 2 !");
                }
                replayButton.setText("Rejouer");
                isGamePlayable = false;
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

        return -1;
    }

    /*
    // Retourne 2 pour victoire joueur 2, 1 pour victoire joueur 1 et 0 pour match nul, -1 pour partie non terminee
    private int gameFinished() {
        boolean allCasesPlayed = true;
        int i = 0;
        while (i != 9 && allCasesPlayed){
            if(in[i] == 0){
                allCasesPlayed = false;
            }
            i++;
        }

        System.out.println("Cas joueur 1 : " + hasPlayerWon(1));
        System.out.println("Cas joueur 2 : " + hasPlayerWon(2));

        if(allCasesPlayed & (hasPlayerWon(1)!=1) & (hasPlayerWon(2)!=1)) {
            return 0;
        }
        else if((hasPlayerWon(1)==-1)){
            System.out.println("Joueur 1 a gagné");
            return 1;
        }
        else if ((hasPlayerWon(2)==1)) {
            System.out.println("Joueur 2 a gagné");
            return 2;
        }
        return -1;
    }
    */

    private int getVictoryForPlayerToken(int playerToken) {
        return switch (playerToken) {
            case -1 -> 1;
            case 1 -> 2;
            default -> -1;
        };
    }


    private int hasPlayerWon(int i) {
        int x = 1;
        if(i == 1) {
            x = -1;
        }

        if (in[0] == x) {
            if ((in[1] == x) & (in[2] == x)) {
                highlightCases(0, 1, 2, true);
                return x;
            } else if ((in[3] == x) & (in[6] == x)) {
                highlightCases(0, 3, 6, true);
                return x;
            } else if ((in[4] == x) & (in[8] == x)) {
                highlightCases(0, 4, 8, true);
                return x;
            }
        }
        else if ((in[1] == x) & (in[4] == x) & (in[7] == x)) {
            highlightCases(1, 4, 7, true);
            return x;
        }
        else if ((in[3] == x) & (in[4] == x) & (in[5] == x)) {
            highlightCases(3, 4, 5, true);
            return x;
        }
        else if ((in[6] == x) & (in[7] == x) & (in[8] == x)) {
            highlightCases(6, 7, 8, true);
            return x;
        }
        else if (in[2] == x) {
            if ((in[5] == x) & (in[8] == x)) {
                highlightCases(2, 5, 8, true);
                return x;
            } else if ((in[4] == x) & (in[6] == x)) {
                highlightCases(2, 4, 6, true);
                return x;
            }
        }
        else {
            return 0;
        }
        return 10;
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

    private void fillCircleTable() {
        for (int i=0; i<3; i++) {
            for (int j=0; j<3; j++) {
                ImageView image = new ImageView(new Image(Main.class.getResource("/images/orange_circle.jpg").toString()));
                image.setFitHeight(90.0);
                image.setFitWidth(90);
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
                image.setFitHeight(90);
                image.setFitWidth(90);
                image.setPreserveRatio(true);
                image.setVisible(false);
                Insets insets = new Insets(5);
                gridPane.setMargin(image, insets);
                imageViewCrossTable[i][j] = image;
                gridPane.add(imageViewCrossTable[i][j], i , j);
            }
        }
    }
}
