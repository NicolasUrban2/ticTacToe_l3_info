package controller;

import javafx.fxml.FXML;

public class TutorialScreenLayoutController {
    private MainController mainController = MainController.getInstance();

    @FXML
    private void onRetourButtonClicked() {
        mainController.changeView("welcomeScreenLayout");
    }
}
