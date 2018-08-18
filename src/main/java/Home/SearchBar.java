/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Home;

import Home.HomeTab.FMetadata;
import Home.HomeTab.FileInfo;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

/**
 *
 * @author t_kimka
 */
public class SearchBar extends TextField {
    public SearchBar(/**HomeTab ht**/) {
        addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
         @Override
         public void handle(KeyEvent event) {
            System.out.println(getText());
            if(!getText().isEmpty()) {
                for (int i = 0; i < HomeTab.allMetadata.size(); i++ ){ 
                    ObservableList<FMetadata> tab = HomeTab.allMetadata.get(i);
                    final int tabIndex = i;
                    tab.forEach((FMetadata md) -> {
                        if ( md.fileName.contains(getText())) {
                            System.out.println(md.cloud + " " + md.fileName + " " + md.fileSize);
                            final FileInfo myFile = new FileInfo(md.cloud, md.fileName, md.fileSize, md.lastModified);
//                            ((TableView<FileInfo>)ht.getTabs().get(tabIndex).getContent()).getSelectionModel().select(myFile);
                        }
                    });
                }
            }
         };
      });
    }
}
