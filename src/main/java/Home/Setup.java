package Home;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Setup extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        Home home = new Home();
        Scene scene = new Scene(home);
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