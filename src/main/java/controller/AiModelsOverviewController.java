package controller;

import javafx.beans.binding.ListBinding;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import main.Main;

import java.io.File;
import java.net.URL;
import java.util.*;

public class AiModelsOverviewController implements Initializable {
    @FXML
    private GridPane gridPane;

    @FXML
    private Button deleteButton;

    private List<String> modelsFilesNames = new ArrayList<>();

    private List<String> modelFilesToDelete = new ArrayList<>();

    private String modelsPath = "./resources/models";

    private MainController mainController = MainController.getInstance();

    private void searchModels() {
        this.modelsFilesNames.clear();
        File modelsDirectory = new File(modelsPath);
        this.modelsFilesNames.addAll(Arrays.asList(modelsDirectory.list()));
    }

    private void removeModelsToDeleteFromModelsFilesNames() {
        modelsFilesNames.removeAll(modelFilesToDelete);
    }

    @FXML
    public void onDeleteButtonClick() {
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
    public void onOkButtonClick() {
        deleteModelsFromModelFilesToDelete();
        closeWindow();
    }

    @FXML
    public void onSauvegarderButtonClick() {
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
        for (int i = 0; i < modelsFilesNames.size(); i++) {
            CheckBox checkBox = new CheckBox();
            checkBox.setScaleX(1.5);
            checkBox.setScaleY(1.5);
            checkBox.setAlignment(Pos.CENTER);

            Label label = new Label(modelsFilesNames.get(i));
            label.setFont(new Font(16));

            gridPane.addRow(i, label, checkBox);
            gridPane.setHalignment(label, HPos.CENTER);
        }
    }

    private void clearGridPane() {
        gridPane.getChildren().clear();
        gridPane.getRowConstraints().clear();
        gridPane.getColumnConstraints().clear();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

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
}
