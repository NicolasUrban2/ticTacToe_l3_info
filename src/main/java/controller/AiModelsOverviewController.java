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

import java.io.File;
import java.net.URL;
import java.util.*;

public class AiModelsOverviewController implements Initializable {
    @FXML
    private GridPane gridPane;

    @FXML
    private Button deleteButton;

    private List<String> modelsFilesNames = new ArrayList<>();

    private String modelsPath = "./resources/models";

    private void searchModels() {
        this.modelsFilesNames.clear();
        File modelsDirectory = new File(modelsPath);
        this.modelsFilesNames.addAll(Arrays.asList(modelsDirectory.list()));
    }

    @FXML
    public void onDeleteButtonClick() {
        List<String> modelsToDeleteList = new ArrayList<>();
        for (int i = 0; i < modelsFilesNames.size(); i++) {
            if (((CheckBox) getNodeFromGridPane(1, i)).isSelected()) {
                modelsToDeleteList.add(modelsFilesNames.get(i));
                System.out.println(modelsFilesNames.get(i));
                File file = new File(modelsPath + "/" + modelsFilesNames.get(i));
                file.delete();
            }
        }
        searchModels();
        clearGridPane();
        fillGridPane();
        showDeletedFiles();
    }

    public void showDeletedFiles() {
        // Affiche une fenêtre qui dit que la liste des fichiers ont bien été crées
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
