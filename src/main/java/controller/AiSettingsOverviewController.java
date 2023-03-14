package controller;

import ai.Config;
import ai.ConfigFileLoader;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import main.HelloApplication;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AiSettingsOverviewController implements Initializable {
    @FXML
    private TextField textFieldFacileNumberOfHiddenLayers;

    @FXML
    private TextField textFieldFacileHiddenLayerSize;

    @FXML
    private TextField textFieldFacileLearningRate;

    @FXML
    private TextField textFieldMoyenNumberOfHiddenLayers;

    @FXML
    private TextField textFieldMoyenHiddenLayerSize;

    @FXML
    private TextField textFieldMoyenLearningRate;

    @FXML
    private TextField textFieldDifficileNumberOfHiddenLayers;

    @FXML
    private TextField textFieldDifficileHiddenLayerSize;

    @FXML
    private TextField textFieldDifficileLearningRate;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ConfigFileLoader cfl = new ConfigFileLoader();
        cfl.loadConfigFile("./resources/config.txt");
        Config configFacile = cfl.get("F");
        textFieldFacileNumberOfHiddenLayers.setText(Integer.toString(configFacile.numberOfhiddenLayers));
        textFieldFacileHiddenLayerSize.setText(Integer.toString(configFacile.hiddenLayerSize));
        textFieldFacileLearningRate.setText(Double.toString(configFacile.learningRate));

        Config configMoyen = cfl.get("M");
        textFieldMoyenNumberOfHiddenLayers.setText(Integer.toString(configMoyen.numberOfhiddenLayers));
        textFieldMoyenHiddenLayerSize.setText(Integer.toString(configMoyen.hiddenLayerSize));
        textFieldMoyenLearningRate.setText(Double.toString(configMoyen.learningRate));

        Config configDifficile = cfl.get("D");
        textFieldDifficileNumberOfHiddenLayers.setText(Integer.toString(configDifficile.numberOfhiddenLayers));
        textFieldDifficileHiddenLayerSize.setText(Integer.toString(configDifficile.hiddenLayerSize));
        textFieldDifficileLearningRate.setText(Double.toString(configDifficile.learningRate));
    }
}
