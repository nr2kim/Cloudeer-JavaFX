/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Home;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 *
 * @author t_kimka
 */
public class Home extends Pane {
    public Home() {
        super();
        this.getStylesheets().add("/css/main.css");
        HomeTab ht = new HomeTab();
        ht.setLayoutY(40);
        OptionalPane op = new OptionalPane(ht);
        op.setLayoutY(40);
        op.setLayoutX(ht.getWidth());
        op.setPrefWidth(ht.getWidth());
        
        TopPane tp = new TopPane(op);
        tp.setPrefHeight(40);
        
        this.getChildren().addAll(tp, ht, op);
        this.widthProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (op.isSearching) {
                    ht.setPrefWidth((Double) newValue * ht.getWidth() / (op.getWidth() + ht.getWidth()));
                    op.setPrefWidth((Double) newValue * op.getWidth() / (op.getWidth() + ht.getWidth()));
                } else {
                    ht.setPrefWidth((Double) newValue);
                }
                op.setLayoutX(ht.getWidth());
                tp.setPrefWidth((Double) newValue);
            }
        });
        
        this.heightProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                ht.setPrefHeight((Double) newValue - 40);
                op.setPrefHeight((Double) newValue - 40);
            }
            
        });
    }
    
    
}