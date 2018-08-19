/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Home;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

/**
 *
 * @author t_kimka
 */
public class TopPane extends Pane {

    public TopPane(HomeTab ht) {
        super();
        this.setPrefHeight(40);
        this.setPrefWidth(Home.homeScreen.getWidth());
        
        SearchResult sr = new SearchResult();
        SearchBar sb = new SearchBar(ht, sr);
        FontAwesomeIconView searchIcon = new FontAwesomeIconView(FontAwesomeIcon.SEARCH);
        searchIcon.setLayoutX(35);
        searchIcon.setLayoutY(24);
        
        FontAwesomeIconView upIcon = new FontAwesomeIconView(FontAwesomeIcon.ANGLE_UP);
        upIcon.setLayoutX(Home.homeScreen.getWidth()*0.7 + 30);
        upIcon.setLayoutY(24);
        
        FontAwesomeIconView downIcon = new FontAwesomeIconView(FontAwesomeIcon.ANGLE_DOWN);
        downIcon.setLayoutX(Home.homeScreen.getWidth()*0.7 + 45);
        downIcon.setLayoutY(24);
        
        Button b = new Button("Login");
        b.getStyleClass().add("button");
        b.setLayoutX(Home.homeScreen.getWidth()*0.7 + 50);
        b.setLayoutY(24);
        b.setMnemonicParsing(false);
        b.setPrefHeight(Home.homeScreen.getHeight()*0.8);
        b.setPrefWidth(Home.homeScreen.getWidth()*0.08);
        
        this.getChildren().addAll(sb, searchIcon, upIcon, downIcon, sr, b);
    }
}