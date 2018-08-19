/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Home;

import javafx.geometry.Rectangle2D;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

/**
 *
 * @author t_kimka
 */
public class Home extends VBox {

    public static Rectangle2D homeScreen;
    public Home() {
        super();
        Rectangle2D screen = Screen.getPrimary().getVisualBounds();
        homeScreen = new Rectangle2D(200, 200, screen.getWidth()/2, screen.getHeight()/2 - 35);
        this.setPrefHeight(homeScreen.getHeight());
        this.setPrefWidth(homeScreen.getWidth());
        this.setMinHeight(homeScreen.getMinY());
        this.setMinWidth(homeScreen.getMinX());
        this.getStylesheets().add("/css/main.css");
        
        HomeTab ht = new HomeTab();
        TopPane tp = new TopPane(ht);
        
        this.getChildren().addAll(tp, ht);
    }
}