package controller;

import ai.Config;
import ai.ConfigFileLoader;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AiSettingsOverviewController implements Initializable, CanSetDarkmode {
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
    @FXML
    private Button okButton;
    @FXML
    private Button saveButton;
    @FXML
    private Label errorLabel;
    @FXML
    private AnchorPane backgroundAnchorPane;
    @FXML
    private Label lLabel;
    @FXML
    private Label hLabel;
    @FXML
    private Label lrLabel;
    @FXML
    private Label facileLabel;
    @FXML
    private Label moyenLabel;
    @FXML
    private Label difficileLabel;
    private MainController mainController = MainController.getInstance();
    private final String[] regexTable = {"^([0-9]+)$", "^([0-9]+)\\.([0-9]+)$"};

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mainController.registerAsDarkModeObserver(this);
        displayConfigContent();
        setTextVerifiersToAllTextFields();
    }

    private void setTextVerifiersToAllTextFields() {
        setEventHandlerToTextField(0, textFieldFacileHiddenLayerSize);
        setEventHandlerToTextField(0, textFieldFacileNumberOfHiddenLayers);
        setEventHandlerToTextField(1, textFieldFacileLearningRate);

        setEventHandlerToTextField(0, textFieldMoyenHiddenLayerSize);
        setEventHandlerToTextField(0, textFieldMoyenNumberOfHiddenLayers);
        setEventHandlerToTextField(1, textFieldMoyenLearningRate);

        setEventHandlerToTextField(0, textFieldDifficileHiddenLayerSize);
        setEventHandlerToTextField(0, textFieldDifficileNumberOfHiddenLayers);
        setEventHandlerToTextField(1, textFieldDifficileLearningRate);
    }

    // Permet d'empêcher la validation des champs lorsque le contenu n'est pas aux normes
    private void setEventHandlerToTextField(int regexIndex, TextField textField) {
        EventHandler<KeyEvent> eventHandler = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(textField.getText().matches(regexTable[regexIndex])) {
                    // Champ correct
                    errorLabel.setText("");
                    textField.setStyle("-fx-border-color: none");
                    okButton.setDisable(false);
                    saveButton.setDisable(false);
                } else {
                    // Champ incorrect
                    String errorMessage;
                    switch (regexIndex) {
                        case 0:
                            errorMessage = "Erreur : Les champs doivent contenir des entiers";
                            break;
                        default :
                            errorMessage = "Erreur : Les champs au milieu doivent contenir un nombre à point";
                    }
                    errorLabel.setText(errorMessage);
                    textField.setStyle("-fx-border-color: red");
                    okButton.setDisable(true);
                    saveButton.setDisable(true);
                }
            }
        };
        textField.addEventHandler(KeyEvent.KEY_RELEASED, eventHandler);
    }

    private void displayConfigContent() {
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

    @FXML
    private void onSaveButtonClick(){
        int fl, fh, ml, mh, dl, dh;
        double flr, mlr, dlr;
        fl = Integer.parseInt(textFieldFacileNumberOfHiddenLayers.getText());
        ml = Integer.parseInt(textFieldMoyenNumberOfHiddenLayers.getText());
        dl = Integer.parseInt(textFieldDifficileNumberOfHiddenLayers.getText());

        fh = Integer.parseInt(textFieldFacileHiddenLayerSize.getText());
        mh = Integer.parseInt(textFieldMoyenHiddenLayerSize.getText());
        dh = Integer.parseInt(textFieldDifficileHiddenLayerSize.getText());

        flr = Double.parseDouble(textFieldFacileLearningRate.getText());
        mlr = Double.parseDouble(textFieldMoyenLearningRate.getText());
        dlr = Double.parseDouble(textFieldDifficileLearningRate.getText());

        String newContent = "F:" + fh + ":" + flr + ":" + fl + "\n" +
                            "M:" + mh + ":" + mlr + ":" + ml + "\n" +
                            "D:" + dh + ":" + dlr + ":" + dl;

        try {
            File file = new File("./resources/config.txt");

            file.createNewFile();

            FileWriter fw = new FileWriter(file, false);
            fw.write(newContent);
            fw.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        mainController.getDifficultyChoiceController().updateAllDifficultyStatus();
    }

    @FXML
    private void onOkButtonClick(){
        onSaveButtonClick();
        closeWindow();
    }

    private void closeWindow(){
        mainController.removeFromDarkModeObservers(this);
        Stage stage = (Stage) okButton.getScene().getWindow();
        mainController.enableMainWindow();
        stage.close();
    }

    @Override
    public void setDarkMode(boolean applyDarkMode) {
        if(applyDarkMode) {
            backgroundAnchorPane.setStyle(mainController.getDarkStyle1());
            facileLabel.setTextFill(Color.WHITE);
            moyenLabel.setTextFill(Color.WHITE);
            difficileLabel.setTextFill(Color.WHITE);
            lLabel.setTextFill(Color.WHITE);
            lrLabel.setTextFill(Color.WHITE);
            hLabel.setTextFill(Color.WHITE);
        } else {
            backgroundAnchorPane.setStyle(mainController.getBrightStyle1());
            facileLabel.setTextFill(Color.BLACK);
            moyenLabel.setTextFill(Color.BLACK);
            difficileLabel.setTextFill(Color.BLACK);
            lLabel.setTextFill(Color.BLACK);
            lrLabel.setTextFill(Color.BLACK);
            hLabel.setTextFill(Color.BLACK);
        }
    }
}
