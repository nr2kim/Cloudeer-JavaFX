/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Home;

import Home.HomeTab.FMetadata;
import Home.HomeTab.FileInfo;
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
    public SearchBar(HomeTab ht, SearchResult sr) {
        super();
        this.getStyleClass().add("searchBar");
        
        this.setLayoutX(20);
        this.setLayoutY(7);
        this.setPrefHeight(25);
        this.setPrefWidth(Home.homeScreen.getWidth()*0.7);
        
        this.textProperty().addListener((obj, oldVal, newVal) -> {
            System.out.println(newVal);
            
            if(!newVal.isEmpty()) {
                int result = 0;
                for (int i = 0; i < HomeTab.allMetadata.size(); i++ ){ 
                    ObservableList<FMetadata> tab = HomeTab.allMetadata.get(i);
                    final int tabIndex = i;
                    for(int j = 0; j < tab.size(); j++ ) {
                        FMetadata md = tab.get(j);
                        if ( md.fileName.contains(newVal)) {
                            result++;
                            System.out.println(md.cloud + " " + md.fileName + " " + md.fileSize);
                            final FileInfo myFile = new FileInfo(md.cloud, md.fileName, md.fileSize, md.lastModified);
//                            ht.show
                            sr.showResult(result);
                        }
                    }
                }
            }
        });
    }
}