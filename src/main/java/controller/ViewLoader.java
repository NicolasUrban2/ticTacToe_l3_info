package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import main.Main;
import model.ViewAndController;

import java.io.IOException;

public class ViewLoader {
    public static ViewAndController getView(String fxmlFileName) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("/"+fxmlFileName+".fxml"));
        Node node = loader.load();
        Object controller = loader.getController();
        return new ViewAndController(node, controller);
    }

    public static Node getController(String fxmlFileName) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        return loader.getController();
    }
}
