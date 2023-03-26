package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ToggleGroup;

import java.net.URL;
import java.util.ResourceBundle;

public class DifficultyChoiceController implements Initializable {

    @FXML
    CheckBox choixFacile;

    @FXML
    CheckBox choixMoyen;

    @FXML
    CheckBox choixDifficile;

    ToggleGroup toggleGroup = new ToggleGroup();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setChoixFacile();
    }

    public void setChoixFacile() {
        choixMoyen.setSelected(false);
        choixDifficile.setSelected(false);
    }

    public void setChoixMoyen() {
        choixFacile.setSelected(false);
        choixDifficile.setSelected(false);
    }

    public void setChoixDifficile() {
        choixFacile.setSelected(false);
        choixMoyen.setSelected(false);
    }
}