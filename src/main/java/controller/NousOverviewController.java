package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class NousOverviewController implements Initializable, CanSetDarkmode {
    @FXML
    private AnchorPane backgroundAnchorPane;
    @FXML
    private Label firstTitleLabel;
    @FXML
    private Label secondTitleLabel;
    @FXML
    private Label thirdTitleLabel;
    @FXML
    private Label developpersLabel1;
    @FXML
    private Label developpersLabel2;
    @FXML
    private Label cadreLabel;

    private MainController mainController = MainController.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mainController.registerAsDarkModeObserver(this);
    }

    @Override
    public void setDarkMode(boolean applyDarkMode) {
        if(applyDarkMode) {
            backgroundAnchorPane.setStyle(mainController.getDarkStyle1());
            firstTitleLabel.setTextFill(Color.WHITE);
            secondTitleLabel.setTextFill(Color.WHITE);
            thirdTitleLabel.setTextFill(Color.WHITE);
            developpersLabel1.setTextFill(Color.WHITE);
            developpersLabel2.setTextFill(Color.WHITE);
            cadreLabel.setTextFill(Color.WHITE);
        } else {
            backgroundAnchorPane.setStyle(mainController.getBrightStyle1());
            firstTitleLabel.setTextFill(Color.BLACK);
            secondTitleLabel.setTextFill(Color.BLACK);
            thirdTitleLabel.setTextFill(Color.BLACK);
            developpersLabel1.setTextFill(Color.BLACK);
            developpersLabel2.setTextFill(Color.BLACK);
            cadreLabel.setTextFill(Color.BLACK);
        }
    }
}
