package controller;

import ai.Coup;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import main.Main;

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

    private ImageView[][] imageViewCircleTable = new ImageView[3][3];

    private ImageView[][] imageViewCrossTable = new ImageView[3][3];

    private int xClickedCase;
    private int yClickedCase;
    private boolean playerRound = true; // True = Player 1 (Left)
                                        // False = Player 2 (Right)
    private Boolean[][] playedCases = new Boolean[3][3];
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Coup coup = new Coup(9,"");
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
                image.setFitHeight(100.0);
                image.setFitWidth(100.0);
                image.setPreserveRatio(true);
                gridPane.add(image, i , j);
                image.setOnMouseClicked(event -> {
                    xClickedCase = GridPane.getColumnIndex(image);
                    yClickedCase = GridPane.getRowIndex(image);
                    playedCases[xClickedCase][yClickedCase] = false;
                    System.out.println(xClickedCase + " " + yClickedCase);
                    if(playerRound) {
                        playerRound = false;
                        playedCases[xClickedCase][yClickedCase] = true;
                        imageViewCrossTable[xClickedCase][yClickedCase].setVisible(true);
                    }
                    else {
                        playerRound = true;
                        playedCases[xClickedCase][yClickedCase] = true;
                        imageViewCircleTable[xClickedCase][yClickedCase].setVisible(true);
                    }
                });
            }
        }
    }

    public void fillCircleTable() {
        for (int i=0; i<3; i++) {
            for (int j=0; j<3; j++) {
                ImageView image = new ImageView(new Image(Main.class.getResource("/orange_circle.jpg").toString()));
                image.setFitHeight(100.0);
                image.setFitWidth(100.0);
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
                image.setFitHeight(100.0);
                image.setFitWidth(100.0);
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
