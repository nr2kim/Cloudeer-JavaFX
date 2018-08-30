package Home;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public class TopPane extends Pane {

    public TopPane(OptionalPane op) {
        super();
        this.getStyleClass().add("TopPane");
        SearchResult sr = new SearchResult();
        sr.setLayoutY(10);
        
        SearchBar sb = new SearchBar(op, sr);
        sb.setLayoutX(20);
        sb.setLayoutY(7);
        sb.setPrefHeight(25);
        
        FontAwesomeIconView searchIcon = new FontAwesomeIconView(FontAwesomeIcon.SEARCH);
        searchIcon.setLayoutX(35);
        searchIcon.setLayoutY(24);
        
        FontAwesomeIconView upIcon = new FontAwesomeIconView(FontAwesomeIcon.ANGLE_UP);
        upIcon.setLayoutY(24);

        FontAwesomeIconView downIcon = new FontAwesomeIconView(FontAwesomeIcon.ANGLE_DOWN);
        downIcon.setLayoutY(24);

        Button b = new Button("Login");
        b.getStyleClass().add("button");
        b.setLayoutY(24);
        b.setMnemonicParsing(false);
        
        this.widthProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                sr.setLayoutX((Double)newValue*0.7 + 55);
                sb.setPrefWidth((Double)newValue*0.7);
                upIcon.setLayoutX((Double)newValue*0.7 + 30);
                downIcon.setLayoutX((Double)newValue*0.7 + 45);
                b.setLayoutX((Double)newValue*0.7 + 50);
        
                b.setPrefHeight((Double)newValue*0.8);
                b.setPrefWidth((Double)newValue*0.08);

            }
        });
        this.getChildren().addAll(sb, searchIcon, upIcon, downIcon, sr, b);
    }
}