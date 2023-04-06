package controller;

import ai.Config;
import ai.ConfigFileLoader;
import ai.MultiLayerPerceptron;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import main.Main;
import model.GameSettings;
import model.ViewAndController;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DifficultyChoiceController implements Initializable, CanSetDarkmode {

    @FXML
    CheckBox choixFacile;
    @FXML
    CheckBox choixMoyen;
    @FXML
    CheckBox choixDifficile;
    @FXML
    private Button playButton;
    @FXML
    private AnchorPane backgroundAnchorPane;
    @FXML
    private Label titleLabel;
    @FXML
    private Label labelStatusFacile;
    @FXML
    private Label labelStatusMoyen;
    @FXML
    private Label labelStatusDifficile;


    private String choiceSelected;

    private GameSettings gameSettings = GameSettings.getInstance();
    private MainController mainController = MainController.getInstance();

    @FXML
    private Button accueilButton;

    ToggleGroup toggleGroup = new ToggleGroup();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mainController.registerAsDarkModeObserver(this);

        mainController.setDifficultyChoiceController(this);
        accueilButtonInitialization();

        updateAllDifficultyStatus();

        setChoixFacile();
    }

    public void updateAllDifficultyStatus() {
        updateDifficultyStatus("F", labelStatusFacile);
        updateDifficultyStatus("M", labelStatusMoyen);
        updateDifficultyStatus("D", labelStatusDifficile);
    }

    private void updateDifficultyStatus(String difficulty, Label label) {
        String fileName = recupFileName(difficulty);
        if(verifyFileIntegrity(fileName)) {
            label.setText("Prêt");
            label.setTextFill(Color.GREEN);
        } else if(fileExists(fileName)){
            label.setText("Modèle corrompu");
            label.setTextFill(Color.ORANGE);
        } else {
            label.setText("Non entraîné");
            label.setTextFill(Color.ORANGE);
        }
    }

    private void accueilButtonInitialization() {
        ImageView house = new ImageView(Main.class.getResource("/images/house.png").toString());
        accueilButton.setGraphic(house);
        house.setFitHeight(20);
        house.setFitWidth(20);
    }

    @FXML
    private void onAccueilButtonClick() {
        mainController.changeView("welcomeScreenLayout");
    }

    public void setChoixFacile() {
        choixFacile.setSelected(true);
        choixMoyen.setSelected(false);
        choixDifficile.setSelected(false);
        choiceSelected = "F";
    }

    public void setChoixMoyen() {
        choixMoyen.setSelected(true);
        choixFacile.setSelected(false);
        choixDifficile.setSelected(false);
        choiceSelected = "M";
    }

    public void setChoixDifficile() {
        choixDifficile.setSelected(true);
        choixFacile.setSelected(false);
        choixMoyen.setSelected(false);
        choiceSelected = "D";
    }

    public void onJouerButtonClick() {
        gameSettings.setDifficulty(choiceSelected);
        searchFile();
        // Lancer le jeu
    }

    private void searchFile() {
        String difficulty = gameSettings.getDifficulty();

        String fileName = recupFileName(difficulty);

        if(fileExists(fileName)) {
            MultiLayerPerceptron multiLayerPerceptron = MultiLayerPerceptron.load("./resources/models/"+ fileName);
            if(!verifyFileIntegrity(fileName)) {
                System.out.println("Fichier de modèle corrompu, suppression du fichier.");
                File file = new File("./resources/models/"+ fileName);
                file.delete();
                callTrainingWindow(difficulty);
            } else {
                gameSettings.setMultiLayerPerceptron(multiLayerPerceptron);
                mainController.changeView("gameScreenLayout");
            }
        }
        else {
            callTrainingWindow(difficulty);
        }
    }

    public String recupFileName(String difficulty) {
        ConfigFileLoader cfl = new ConfigFileLoader();
        cfl.loadConfigFile("./resources/config.txt");
        Config config = cfl.get(difficulty);

        int l = config.numberOfhiddenLayers;
        int h = config.hiddenLayerSize;
        double lr = config.learningRate;

        return "model_" + l + "_" + h + "_" + lr + ".srl";
    }

    private boolean verifyFileIntegrity(String fileName) {
        if(fileExists(fileName)) {
            MultiLayerPerceptron multiLayerPerceptron = MultiLayerPerceptron.load("./resources/models/"+ fileName);
            if(multiLayerPerceptron != null) {
                return true;
            }
        }
        return false;
    }

    private void callTrainingWindow(String difficulty) {
        try {
            ViewAndController viewAndController = ViewLoader.getView("aiLearningOverview");
            Object controller = viewAndController.controller;
            Scene scene = new Scene((AnchorPane) viewAndController.node);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Entraînement de l'IA");
            stage.setResizable(false);
            stage.show();

            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent windowEvent) {
                    if(controller instanceof AiLearningOverviewController) {
                        if(!((AiLearningOverviewController) controller).isTrainingComplete()) {
                            System.out.println("Entraînement incomplet -> suppression du modèle");
                            File file = new File("./resources/models/"+recupFileName(difficulty));
                            file.delete();
                        }
                    }
                    if(controller != null) {
                        if(controller instanceof CanSetDarkmode) {
                            mainController.removeFromDarkModeObservers((CanSetDarkmode) controller);
                        }
                    }
                }
            });
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private boolean fileExists(String fileName) {
        File directory = new File("./resources/models");
        List<String> modelsFilesList = List.of(directory.list());

        for (String file : modelsFilesList) {
            if(file.equals(fileName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void setDarkMode(boolean applyDarkMode) {
        if(applyDarkMode) {
            backgroundAnchorPane.setStyle(mainController.getDarkStyle1());
            choixFacile.setTextFill(Color.WHITE);
            choixMoyen.setTextFill(Color.WHITE);
            choixDifficile.setTextFill(Color.WHITE);
            titleLabel.setTextFill(Color.WHITE);
        } else {
            backgroundAnchorPane.setStyle(mainController.getBrightStyle1());
            choixFacile.setTextFill(Color.BLACK);
            choixMoyen.setTextFill(Color.BLACK);
            choixDifficile.setTextFill(Color.BLACK);
            titleLabel.setTextFill(Color.BLACK);
        }
    }
}