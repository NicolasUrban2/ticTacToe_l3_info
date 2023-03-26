package controller;


import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import main.Main;

import java.io.IOException;

public class MainController {
    private static MainController INSTANCE;
    private Stage root;
    private AnchorPane rootAnchorPane;

    private MainLayoutController mainLayoutController;

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
        //System.out.println(mainLayoutController);
        this.rootAnchorPane = (AnchorPane) loader.load();
        mainLayoutController = loader.getController();
        Scene scene = new Scene(rootAnchorPane);
        root.setTitle("Tic Tac Toe");
        root.setScene(scene);
        root.show();
    }

    public AnchorPane getRootAnchorPane() {
        return rootAnchorPane;
    }

    public Stage getRoot() {
        return root;
    }

    public void enableMainWindow() {
        mainLayoutController.enableMainWindow();
    }

    public void disableMainWindow() {
        mainLayoutController.disableMainWindow();
    }

    public void changeView(String viewName) {
        mainLayoutController.changeView(viewName);
    }
}
