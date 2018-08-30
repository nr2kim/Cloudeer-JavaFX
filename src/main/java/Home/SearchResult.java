package Home;

import javafx.scene.control.TextField;

public class SearchResult extends TextField {
    public SearchResult() {
        super();
        this.getStyleClass().add("searchResult");        
        this.setEditable(false);
    }
    
    public void showResult(int r) {
        this.setText(r + " found" );
    }
    
    public void hideResult() {
        this.clear();
    }
}
