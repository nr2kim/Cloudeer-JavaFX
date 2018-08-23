package Home;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author t_kimka
 */
public class Setup extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        HomeFXMLLoader hl = new HomeFXMLLoader();
        Scene scene = new Scene(hl);
        primaryStage.setTitle("Cloudeer");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(500);
        primaryStage.setMinHeight(200);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}