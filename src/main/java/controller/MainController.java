package controller;


import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import main.Main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainController {
    private static MainController INSTANCE;
    private Stage root;
    private AnchorPane rootAnchorPane;
    private MainLayoutController mainLayoutController;
    private DifficultyChoiceController difficultyChoiceController;
    private List<CanSetDarkmode> darkModeObserversList = new ArrayList<>();
    private boolean isDarkModeToggleButtonSelected;
    private final String darkStyle1 = "-fx-background-color: rgba(0, 0, 0, 0.8)";
    private final String darkStyle2 = "-fx-background-color: rgba(0, 0, 0, 0.75)";
    private final String darkStyle3 = "-fx-background-color: rgba(0, 0, 0, 0.4)";
    private final String brightStyle1 = "-fx-background-color: rgba(0, 0, 0, 0)";
    private final String brightStyle2 = "-fx-background-color: rgba(0, 0, 0, 1)";
    private final String rootDefaultStyle = "-fx-background-color: rgba(0, 0, 0, 0.05)";

    // MainController est un singleton
    private MainController() {

    }
    public static MainController getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new MainController();
        }
        return INSTANCE;
    }

    public void start(Stage stage) throws IOException {
        root = stage;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("/mainLayout.fxml"));
        this.rootAnchorPane = (AnchorPane) loader.load();
        rootAnchorPane.setStyle(rootDefaultStyle);
        mainLayoutController = loader.getController();
        Scene scene = new Scene(rootAnchorPane);
        root.setTitle("Tic Tac Toe");
        root.setScene(scene);
        root.setResizable(false);
        root.show();
    }

    public AnchorPane getRootAnchorPane() {
        return rootAnchorPane;
    }

    public void enableMainWindow() {
        mainLayoutController.enableMainWindow();
    }

    public void disableMainWindow() {
        mainLayoutController.disableMainWindow();
    }

    // Changer la vue actuelle
    public void changeView(String viewName) {
        mainLayoutController.changeView(viewName);
    }

    public DifficultyChoiceController getDifficultyChoiceController() {
        return difficultyChoiceController;
    }

    // Changer la difficulté
    public void setDifficultyChoiceController(DifficultyChoiceController difficultyChoiceController) {
        this.difficultyChoiceController = difficultyChoiceController;
    }

    public void registerAsDarkModeObserver(CanSetDarkmode object) {
        if(object != null) {
            darkModeObserversList.add(object);
            setDarkModeToAllObservers(isDarkModeToggleButtonSelected());
        }
    }

    public void removeFromDarkModeObservers(CanSetDarkmode object) {
        if(object != null) {
            if(darkModeObserversList.contains(object)) {
                darkModeObserversList.remove(object);
            }
        }
    }

    public void setDarkModeToAllObservers(boolean applyDarkMode) {
        for (CanSetDarkmode object:darkModeObserversList) {
            object.setDarkMode(applyDarkMode);
        }
    }

    public String getDarkStyle1() {
        return darkStyle1;
    }

    public String getDarkStyle2() {
        return darkStyle2;
    }

    public String getDarkStyle3() {
        return darkStyle3;
    }

    public String getBrightStyle1() {
        return brightStyle1;
    }

    public String getBrightStyle2() {
        return brightStyle2;
    }

    public boolean isDarkModeToggleButtonSelected() {
        return isDarkModeToggleButtonSelected;
    }

    // Applique ou enlève le DarkMode à toutes les vues enregistrées
    public void setDarkModeToggleButtonSelected(boolean darkModeToggleButtonSelected) {
        isDarkModeToggleButtonSelected = darkModeToggleButtonSelected;
        setDarkModeToAllObservers(isDarkModeToggleButtonSelected());
    }

    public String getRootDefaultStyle() {
        return rootDefaultStyle;
    }
}
