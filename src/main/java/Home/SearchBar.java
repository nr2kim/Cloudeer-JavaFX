package Home;

import Home.CloudTab.FMetadata;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextField;

public class SearchBar extends TextField {
    public SearchBar(OptionalPane op, SearchResult sr) {
        super();
        this.setEditable(true);
        this.getStyleClass().add("searchBar");
        
        this.textProperty().addListener((obj, oldVal, newVal) -> {
            System.out.println(newVal);
            op.showOptionalPane(FXCollections.observableArrayList());
//            if(!newVal.isEmpty()) {
//                ObservableList<FMetadata> result = FXCollections.observableArrayList();
//                for (int i = 0; i < HomeTab.allMetadata.size(); i++ ){ 
//                    ObservableList<FMetadata> tab = HomeTab.allMetadata.get(i);
//                    final int tabIndex = i;
//                    for(int j = 0; j < tab.size(); j++ ) {
//                        FMetadata md = tab.get(j);
//                        this.checkFolder(result, md, newVal);
//                    }
//                }
//                sr.showResult(result.size());
//                op.showOptionalPane(result);
//            } else {
//                op.hideOptionalPane();
//                sr.hideResult();
//            }
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
            System.out.println(md.fileName);
            result.add(md);
        }
    }
}
