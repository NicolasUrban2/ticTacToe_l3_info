package controller;

import ai.Config;
import ai.ConfigFileLoader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ToggleGroup;
import model.GameSettings;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DifficultyChoiceController implements Initializable {

    @FXML
    CheckBox choixFacile;

    @FXML
    CheckBox choixMoyen;

    @FXML
    CheckBox choixDifficile;

    private String choiceSelected;

    private GameSettings gameSettings = GameSettings.getInstance();

    ToggleGroup toggleGroup = new ToggleGroup();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setChoixFacile();
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

    public void onButtonClickJouer() {
        gameSettings.setDifficulty(choiceSelected);
        searchFile();
        // Lancer le jeu
    }

    private void searchFile() {
        ConfigFileLoader cfl = new ConfigFileLoader();
        cfl.loadConfigFile("./resources/config.txt");
        Config config = cfl.get(gameSettings.getDifficulty());

        int l = config.numberOfhiddenLayers;
        int h = config.hiddenLayerSize;
        double lr = config.learningRate;

        String fileName = "model_" + l + "_" + h + "_" + lr + ".srl";
        System.out.println(fileName);
        if(fileExists(fileName)) {
            System.out.println("Existe bien");
        }
        else {
            System.out.println("Existe pas");
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
}