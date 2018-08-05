/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SignIn;

import Home.HomeTab.cloudType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

/**
 *
 * @author t_kimka
 */
public class SignInDialog extends Dialog {
    
    private cloudType chosen;
    
    public SignInDialog() {
        this.setHeaderText("Supported Cloud Storage Lists");

        // Set the icon (must be included in the project).
        // dialog.setGraphic(new ImageView(this.getClass().getResource("login.png").toString()));

        // Set the button types.
        ButtonType okButton = new ButtonType("okButton", ButtonBar.ButtonData.OK_DONE);
        this.getDialogPane().getButtonTypes().addAll(okButton, ButtonType.CANCEL);

        // Create the username and password labels and fields.
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(20, 150, 10, 10));
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10);

        ImageView dropboxIcon = new ImageView(cloudType.dropbox.toString());
        dropboxIcon.setFitHeight(40);
        dropboxIcon.setPreserveRatio(true);
        Button dropboxButton = new Button("", dropboxIcon);

        ImageView googleDriveIcon = new ImageView(cloudType.googleDrive.toString());
        googleDriveIcon.setFitHeight(40);
        googleDriveIcon.setPreserveRatio(true);
        Button googleDriveButton = new Button("", googleDriveIcon);

        ImageView oneDriveIcon = new ImageView(cloudType.oneDrive.toString());
        oneDriveIcon.setFitHeight(40);
        oneDriveIcon.setPreserveRatio(true);
        Button oneDriveButton = new Button("", oneDriveIcon);

        vbox.getChildren().add(dropboxButton);
        vbox.getChildren().add(googleDriveButton);
        vbox.getChildren().add(oneDriveButton);

        // Enable/Disable login button depending on whether a username was entered.
        Node okButtonNode = this.getDialogPane().lookupButton(okButton);
        okButtonNode.setDisable(true);

        // Do some validation (using the Java 8 lambda syntax).
        dropboxButton.focusedProperty().addListener((observable, wasFocused, focused) -> {
            okButtonNode.setDisable(!focused);
            this.chosen = cloudType.dropbox;                    
        });
        googleDriveButton.focusedProperty().addListener((observable, wasFocused, focused) -> {
            okButtonNode.setDisable(!focused);     
            this.chosen = cloudType.googleDrive;
        });
        oneDriveButton.focusedProperty().addListener((observable, wasFocused, focused) -> {
            okButtonNode.setDisable(!focused);    
            this.chosen = cloudType.oneDrive;
        });

        this.getDialogPane().setContent(vbox);

        // Convert the result to a username-password-pair when the login button is clicked.
        this.setResultConverter(dialogButton -> {
            if (dialogButton == okButton) {
                return this.chosen;
            }
            return null;
        });

        
    }
}