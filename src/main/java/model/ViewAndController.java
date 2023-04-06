package model;

import javafx.scene.Node;

public class ViewAndController {
    public Node node;
    public Object controller;

    public ViewAndController(Node node, Object controller) {
        this.node = node;
        this.controller = controller;
    }
}
