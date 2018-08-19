/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Home;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

/**
 *
 * @author t_kimka
 */
public class HomeFXMLLoader extends VBox {

    public HomeFXMLLoader() {
        init();
    }

    private void init() {
        try {
            URL fxmlURL = getClass().getResource("/fxml/HomePane.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(fxmlURL);
            fxmlLoader.setRoot(this);
            fxmlLoader.setController(this);
            fxmlLoader.load();
        } catch (IOException ex) {
            Logger.getLogger(HomeFXMLLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void initialize() {
    }
}