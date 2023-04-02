package controller;

import ai.MultiLayerPerceptron;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.*;

public class AiModelsOverviewController implements Initializable, CanSetDarkmode {
    @FXML
    private GridPane gridPane;
    @FXML
    private CheckBox deleteAllCheckbox;
    @FXML
    private AnchorPane backgroundAnchorPane;
    @FXML
    private Label selectAllLabel;
    private List<Label> fileNamesLabelsList = new ArrayList<>();

    private MainController mainController = MainController.getInstance();

    List<CheckBox> checkBoxesList = new ArrayList<>();

    private List<String> modelsFilesNames = new ArrayList<>();

    private List<String> modelFilesToDelete = new ArrayList<>();

    private String modelsPath = "./resources/models";


    private void searchModels() {
        this.modelsFilesNames.clear();

        File modelsDirectory = new File(modelsPath);

        boolean directoryExists = true;

        if(!modelsDirectory.exists()) {
            directoryExists = modelsDirectory.mkdir();
        }

        if(directoryExists) {
            for (String fileName: modelsDirectory.list()) {
                if(fileName.matches("^model_([0-9]+)_([0-9]+)_([0-9]+)\\.([0-9]+)\\.srl$")) {
                    this.modelsFilesNames.add(fileName);
                }
            }
        }
    }

    private void removeModelsToDeleteFromModelsFilesNames() {
        modelsFilesNames.removeAll(modelFilesToDelete);
    }

    @FXML
    private void onDeleteButtonClick() {
        modelFilesToDelete.clear();
        for (int i = 0; i < modelsFilesNames.size(); i++) {
            if (((CheckBox) getNodeFromGridPane(1, i)).isSelected()) {
                modelFilesToDelete.add(modelsFilesNames.get(i));
            }
        }
        removeModelsToDeleteFromModelsFilesNames();
        //searchModels();
        clearGridPane();
        fillGridPane();
    }

    @FXML
    private void onSelectAllCheckboxClick() {
        for (CheckBox checkbox: checkBoxesList) {
            checkbox.setSelected(deleteAllCheckbox.isSelected());
        }
    }

    @FXML
    private void onOkButtonClick() {
        deleteModelsFromModelFilesToDelete();
        closeWindow();
    }

    @FXML
    private void onSauvegarderButtonClick() {
        deleteModelsFromModelFilesToDelete();
    }

    private void deleteModelsFromModelFilesToDelete() {
        for(int i=0; i<modelFilesToDelete.size(); i++) {
            File fileToDelete = new File(modelsPath + "/" + modelFilesToDelete.get(i));
            fileToDelete.delete();
        }
    }

    public void closeWindow(){
        Stage stage = (Stage) gridPane.getScene().getWindow();
        mainController.enableMainWindow();
        stage.close();
    }

    private void fillGridPane() {
        if(modelsFilesNames.size() == 0) {
            Label label = new Label("Aucun fichier trouvé");
            label.setFont(new Font(16));
            fileNamesLabelsList.add(label);

            gridPane.add(label, 0, 0);

            GridPane.setHalignment(label, HPos.CENTER);
        } else {
            for (int i = 0; i < modelsFilesNames.size(); i++) {
                CheckBox checkBox = new CheckBox();
                checkBox.setScaleX(1.5);
                checkBox.setScaleY(1.5);
                checkBox.setAlignment(Pos.CENTER);
                checkBox.setCursor(Cursor.HAND);
                checkBoxesList.add(checkBox);

                Label label = new Label(modelsFilesNames.get(i));
                label.setFont(new Font(16));
                fileNamesLabelsList.add(label);

                gridPane.add(label, 0, i);
                gridPane.add(checkBox, 1, i);
                GridPane.setHalignment(label, HPos.CENTER);
            }
        }
        setDarkMode(mainController.isDarkModeToggleButtonSelected());
    }

    private void clearGridPane() {
        fileNamesLabelsList.clear();
        System.out.println(fileNamesLabelsList.size());
        checkBoxesList.clear();
        gridPane.getChildren().clear();
        gridPane.getRowConstraints().clear();
        gridPane.getColumnConstraints().clear();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mainController.registerAsDarkModeObserver(this);
        searchModels();
        fillGridPane();
    }

    public Node getNodeFromGridPane(int col, int row) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }

    @Override
    public void setDarkMode(boolean applyDarkMode) {
        if(applyDarkMode) {
            backgroundAnchorPane.setStyle(mainController.getDarkStyle1());
            gridPane.setStyle(mainController.getDarkStyle2());
            selectAllLabel.setTextFill(Color.WHITE);
            for (Label label:fileNamesLabelsList) {
                label.setTextFill(Color.WHITE);
            }
        } else {
            backgroundAnchorPane.setStyle(mainController.getBrightStyle1());
            gridPane.setStyle(mainController.getBrightStyle1());
            selectAllLabel.setTextFill(Color.BLACK);
            for (Label label:fileNamesLabelsList) {
                label.setTextFill(Color.BLACK);
            }
        }
    }
}
