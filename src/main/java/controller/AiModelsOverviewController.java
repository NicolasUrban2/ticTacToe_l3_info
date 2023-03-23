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
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class AiModelsOverviewController implements Initializable {
    @FXML
    private GridPane gridPane;

    @FXML
    private Button deleteButton;

    private List<String> modelsFilesNames;

    private String modelsPath = "./resources/models";

    private void searchModels() {
        File modelsDirectory = new File(modelsPath);
        this.modelsFilesNames = List.of(modelsDirectory.list());
    }

    @FXML
    public void onDeleteButtonClick() {
        for (int i = 0; i < modelsFilesNames.size(); i++) {
            if (((CheckBox) getNodeFromGridPane(1, i)).isSelected()) {
                System.out.println(modelsFilesNames.get(i));
                // A faire : supprimer le fichier du même nom
            }
        }
        // A faire : fermer la fenêtre
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
