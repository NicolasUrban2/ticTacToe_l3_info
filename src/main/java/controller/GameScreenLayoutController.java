package controller;

import ai.Coup;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    private Boolean[][] playedCases = new Boolean[3][3];

    private double[] in = new double[9];

    private Coup coup = new Coup(9,"");

    private GameSettings gameSettings = GameSettings.getInstance();

    private MainController mainController = MainController.getInstance();

    private boolean isGamePlayable;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for(int t=0; t<in.length; t++) {
            in[t] = 0;
        }
        isGamePlayable = true;
        playerRound = true;
        replayButton.setVisible(false);
        initializePlayedCases();
        fillEmptyImagesTable();
        fillCircleTable();
        fillCrossTable();
    }

    private void initializePlayedCases() {
        for (int i=0; i<3; i++) {
            for (int j=0; j<3; j++) {
                playedCases[i][j] = false;
            }
        }
    }

    public void fillEmptyImagesTable() {
        for (int i=0; i<3; i++) {
            for (int j=0; j<3; j++) {
                ImageView image = new ImageView(new Image(Main.class.getResource("/white.jpg").toString()));
                image.setFitHeight(90);
                image.setFitWidth(90);
                image.setPreserveRatio(true);
                gridPane.add(image, i , j);
                image.setOnMouseClicked(event -> {
                    if(isGamePlayable) {
                        xClickedCase = GridPane.getColumnIndex(image);
                        yClickedCase = GridPane.getRowIndex(image);
                        playedCases[xClickedCase][yClickedCase] = false;
                        //System.out.println(xClickedCase + " " + yClickedCase);

                        if(playerRound) {
                            playedCases[xClickedCase][yClickedCase] = true;
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
                            playedCases[xClickedCase][yClickedCase] = true;
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

        return 0;
    }

    public void fillCircleTable() {
        for (int i=0; i<3; i++) {
            for (int j=0; j<3; j++) {
                ImageView image = new ImageView(new Image(Main.class.getResource("/orange_circle.jpg").toString()));
                image.setFitHeight(90.0);
                image.setFitWidth(90);
                image.setPreserveRatio(true);
                image.setVisible(false);
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
                imageViewCrossTable[i][j] = image;
                gridPane.add(imageViewCrossTable[i][j], i , j);
            }
        }
    }

    public void setPlayerRound(boolean playerRound) {
        this.playerRound = playerRound;
    }

    public void onGridPaneButtonClick() {
        if (playedCases[xClickedCase][yClickedCase]) {
            if (playerRound) {          // Au tour du joueur 1

            }
            else {                      // Au tour du joueur 2

            }
        }
        else {
            System.out.println("Impossible de jouer cette case, elle a déjà été joué.");
        }
    }

}
