/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Home;

import Home.HomeTab.FMetadata;
import Home.HomeTab.FileInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author t_kimka
 */
public class SearchBar extends TextField {
    public SearchBar(OptionalPane op, SearchResult sr) {
        super();
        this.getStyleClass().add("searchBar");
        
        this.setLayoutX(20);
        this.setLayoutY(7);
        this.setPrefHeight(25);
        this.setPrefWidth(Home.homeScreen.getWidth()*0.7);
        
        this.textProperty().addListener((obj, oldVal, newVal) -> {
            System.out.println(newVal);
            
            if(!newVal.isEmpty()) {
                ObservableList<FMetadata> result = FXCollections.observableArrayList();
                for (int i = 0; i < HomeTab.allMetadata.size(); i++ ){ 
                    ObservableList<FMetadata> tab = HomeTab.allMetadata.get(i);
                    final int tabIndex = i;
                    for(int j = 0; j < tab.size(); j++ ) {
                        FMetadata md = tab.get(j);
                        this.checkFolder(result, md, newVal);
                    }
                }
                sr.showResult(result.size());
                op.showOptionalPane(result);
            } else {
                op.hideOptionalPane();
            }
        });
    }
    
    public void checkFolder(ObservableList<FMetadata> result, FMetadata md, String str) {
        if(md.children != null) {
            md.children.forEach((cList) -> {
                if (cList.fullPath != md.fullPath) {
                    this.checkFolder(result, cList, str);                
                }
            });
        }
        
        if(md.fileName.toLowerCase().contains(str.toLowerCase())) {
            result.add(md);
        }
    }
}
