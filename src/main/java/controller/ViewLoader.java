package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import main.HelloApplication;

import java.io.IOException;

public class ViewLoader {
    public static Node getView(String fxmlFileName) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(HelloApplication.class.getResource("/"+fxmlFileName+".fxml"));
        return loader.load();
    }
}
