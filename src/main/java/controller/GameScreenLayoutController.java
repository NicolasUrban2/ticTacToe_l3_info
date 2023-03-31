package controller;

import ai.Coup;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    private Label tourJ2LabelDroite;

    @FXML
    private Label winOrLose;

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
        winOrLose.setText("");
        for(int t=0; t<in.length; t++) {
            in[t] = 0;
        }
        isGamePlayable = true;
        playerRound = true;
        replayButton.setVisible(false);
        fillEmptyImagesTable();
        fillCircleTable();
        fillCrossTable();
    }

    public void fillEmptyImagesTable() {
        for (int i=0; i<3; i++) {
            for (int j=0; j<3; j++) {
                ImageView image = new ImageView(new Image(Main.class.getResource("/white.jpg").toString()));
                image.setFitHeight(100);
                image.setFitWidth(100);
                image.setPreserveRatio(true);
                image.setOpacity(0);
                gridPane.add(image, i , j);
                image.setOnMouseClicked(event -> {
                    if(isGamePlayable) {
                        xClickedCase = GridPane.getColumnIndex(image);
                        yClickedCase = GridPane.getRowIndex(image);
                        //System.out.println(xClickedCase + " " + yClickedCase);

                        if(playerRound) {
                            imageViewCrossTable[xClickedCase][yClickedCase].setVisible(true);
                            tourJ2LabelDroite.setVisible(true);
                            tourJ1LabelGauche.setVisible(false);
                            in[xClickedCase+yClickedCase*3] = -1;
                            if(gameSettings.getGameMode() == "pve") {

                            } else {
                                playerRound = false;
                            }
                        }
                        else {
                            playerRound = true;
                            imageViewCircleTable[xClickedCase][yClickedCase].setVisible(true);
                            tourJ2LabelDroite.setVisible(false);
                            tourJ1LabelGauche.setVisible(true);
                            in[xClickedCase+yClickedCase*3] = 1;
                        }
                        coup.addInBoard(in);
                        //System.out.println(coup.toString());
                        switch (gameFinished()) {
                            case 0:
                                winOrLose.setText("Match nul");
                                replayButton.setVisible(true);
                                isGamePlayable = false;
                                break;
                            case 1:
                                if(gameSettings.getGameMode()=="pve") {
                                    winOrLose.setText("Victoire !");
                                } else {
                                    winOrLose.setText("Victoire du joueur 1 !");
                                }
                                replayButton.setVisible(true);
                                isGamePlayable = false;
                                break;
                            case 2:
                                if(gameSettings.getGameMode()=="pve") {
                                    winOrLose.setText("Défaite...");
                                } else {
                                    winOrLose.setText("Victoire du joueur 2 !");
                                }
                                replayButton.setVisible(true);
                                isGamePlayable = false;
                                break;
                            default:
                                winOrLose.setText("");
                        }
                    }
                });
            }
        }
    }

    @FXML
    private void onRetourButtonClick() {
        mainController.changeView("welcomeScreenLayout");
    }

    @FXML
    private void onReplayButtonClick() {
        initialize(null, null);
    }

    // Retourne 2 pour victoire joueur 2, 1 pour victoire joueur 1 et 0 pour match nul, -1 pour partie non terminee
    private int gameFinished() {
        Boolean allCasesPlayed = true;
        int i = 0;
        while (i != 9 && allCasesPlayed==true){
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
        else if((hasPlayerWon(1)==1) & (hasPlayerWon(2)!=1)){
            System.out.println("Joueur 1 a gagné");
            return 1;
        }
        else if ((hasPlayerWon(1)!=1) & (hasPlayerWon(2)==1)) {
            System.out.println("Joueur 2 a gagné");
            return 2;
        }
        return -1;
    }

    private int hasPlayerWon(int i) {
        int x = 0;
        if(i == 1) {
            x = -1;
            if (in[0] == x) {
                if ((in[1] == x) & (in[2] == x)) {
                    return 1;
                } else if ((in[3] == x) & (in[6] == x)) {
                    return 1;
                } else if ((in[4] == x) & (in[8] == x)) {
                    return 1;
                }
            } else if ((in[1] == x) & (in[4] == x) & (in[7] == x)) {
                return 1;
            } else if ((in[3] == x) & (in[4] == x) & (in[5] == x)) {
                return 1;
            } else if ((in[6] == x) & (in[7] == x) & (in[8] == x)) {
                return 1;
            } else if (in[2] == x) {
                if ((in[5] == x) & (in[8] == x)) {
                    return 1;
                } else if ((in[4] == x) & (in[6] == x)) {
                    return 1;
                }
            } else {
                return 0;
            }
        }
        else if(i == 2) {
            x = 1;
            if (in[0] == x) {
                if ((in[1] == x) & (in[2] == x)) {
                    return 1;
                } else if ((in[3] == x) & (in[6] == x)) {
                    return 1;
                } else if ((in[4] == x) & (in[8] == x)) {
                    return 1;
                }
            }
            else if ((in[1] == x) & (in[4] == x) & (in[7] == x)) {
                return 1;
            }
            else if ((in[3] == x) & (in[4] == x) & (in[5] == x)) {
                return 1;
            }
            else if ((in[6] == x) & (in[7] == x) & (in[8] == x)) {
                return 1;
            }
            else if (in[2] == x) {
                if ((in[5] == x) & (in[8] == x)) {
                    return 1;
                } else if ((in[4] == x) & (in[6] == x)) {
                    return 1;
                }
            }
            else {
                return 0;
            }
        }
        return 10;
    }

    public void fillCircleTable() {
        for (int i=0; i<3; i++) {
            for (int j=0; j<3; j++) {
                ImageView image = new ImageView(new Image(Main.class.getResource("/orange_circle.jpg").toString()));
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

    public void fillCrossTable() {
        for (int i=0; i<3; i++) {
            for (int j=0; j<3; j++) {
                ImageView image = new ImageView(new Image(Main.class.getResource("/blue_cross.jpg").toString()));
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
