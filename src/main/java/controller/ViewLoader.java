package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import main.Main;

import java.io.IOException;

public class ViewLoader {
    public static Node getView(String fxmlFileName) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("/"+fxmlFileName+".fxml"));
        return loader.load();
    }

    public static Node getController(String fxmlFileName) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        return loader.getController();
    }
}
