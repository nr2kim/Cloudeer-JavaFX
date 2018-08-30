package Home;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class CopyRight extends Pane {

    public CopyRight() {
        super();
        this.setPrefHeight(35);
        Label copyRightText = new Label("@ 2018. All rights reserved. Powered by Kate Kim.");
        copyRightText.setLayoutX(20);
        copyRightText.setLayoutY(10);
        copyRightText.getStyleClass().add("copyRightText");
        this.getChildren().add(copyRightText);
    }
}