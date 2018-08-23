/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Home;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

/**
 *
 * @author t_kimka
 */
public class CopyRight extends Pane {

    public CopyRight() {
        super();
        this.setPrefHeight(35);
        Label l = new Label("@ 2018. All rights reserved. Powered by Kate Kim.");
        l.setLayoutX(20);
        l.setLayoutY(10);
        l.getStyleClass().add("copyRightText");
        this.getChildren().add(l);
    }
}