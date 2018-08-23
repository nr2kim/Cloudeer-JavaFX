/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Home;

import javafx.scene.control.TextField;

/**
 *
 * @author t_kimka
 */
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
