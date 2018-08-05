package Auth;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Home.HomeTab;
import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxAuthInfo;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuth;
import com.dropbox.core.json.JsonReader;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/**
 *
 * @author t_kimka
 */
public class DropboxAuth extends Dialog {
    public DropboxAuth() {
        DbxRequestConfig config;
        final DbxAppInfo appInfo;
        DbxWebAuth webAuth;
        DbxWebAuth.Request webAuthRequest;
        String authorizeUrl;
        
        config = DbxRequestConfig.newBuilder("Cloudeer/1.0.0")
                            .build();
        
        try {
            appInfo = DbxAppInfo.Reader.readFromFile("env/dbx.env");
        } catch (JsonReader.FileLoadException ex) {
            Logger.getLogger(HomeTab.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1); return;
        }
        
        webAuth = new DbxWebAuth(config, appInfo);
        webAuthRequest = DbxWebAuth.newRequestBuilder()
            .withNoRedirect()
            .build();
        authorizeUrl = webAuth.authorize(webAuthRequest);
        
        TextField accessTokenField = new TextField();
        accessTokenField.setPromptText("access token");

        Hyperlink authHyperlink = new Hyperlink(authorizeUrl);
        authHyperlink.setOnAction((ActionEvent e) -> {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI(authorizeUrl));
                } catch (URISyntaxException | IOException ex) {
                    Logger.getLogger(HomeTab.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        ButtonType connectButton = new ButtonType("connect", ButtonBar.ButtonData.OK_DONE);
        this.getDialogPane().getButtonTypes().addAll(connectButton, ButtonType.CANCEL);

        GridPane instruction = new GridPane();
        instruction.setHgap(10);
        instruction.setVgap(10);
        instruction.setPadding(new Insets(20, 150, 10, 10));

        instruction.add(authHyperlink, 0, 0, 3, 1);

        instruction.add(new Label("1."), 0, 1);
        instruction.add(new Label("Click on the above link"), 1, 1);
        instruction.add(new Label("2."), 0, 2);
        instruction.add(new Label("Sign in with your account."), 1, 2);
        instruction.add(new Label("3."), 0, 3);
        instruction.add(new Label("Click \"Allow\""), 1, 3);
        instruction.add(new Label("3."), 0, 4);
        instruction.add(new Label("Copy the authorization code."), 1, 4);
        instruction.add(accessTokenField, 0, 5, 3, 1);
        this.getDialogPane().setContent(instruction);
        
        this.setResultConverter((Object dialogButton) -> {
            if (dialogButton == connectButton) {
                return accessTokenField.getText();
            }
            return null;
        });

        Optional<String> authenticationResult = this.showAndWait();
        authenticationResult.ifPresent((String accessToken) -> {
            final DbxAuthFinish authFinish;
            final DbxAuthInfo authInfo;
            System.out.println("AccessToken=" + accessToken);
            
            try {
                authFinish = webAuth.finishFromCode(accessToken);
            } catch (DbxException ex) {
                System.err.println("Error in DbxWebAuth.authorize: " + ex.getMessage());
                System.exit(1); return;
            }

            System.out.println("Authorization complete.");
            System.out.println("- User ID: " + authFinish.getUserId());
            System.out.println("- Account ID: " + authFinish.getAccountId());
            System.out.println("- Access Token: " + authFinish.getAccessToken());

            // Save auth information to output file.
            authInfo = new DbxAuthInfo(authFinish.getAccessToken(), appInfo.getHost());
            File output = new File("./out/auth_output.txt");
            try {
                DbxAuthInfo.Writer.writeToFile(authInfo, output);
                System.out.println("Saved authorization information to \"" + output.getCanonicalPath() + "\".");
            } catch (IOException ex) {
                System.err.println("Error saving to <auth-file-out>: " + ex.getMessage());
                System.err.println("Dumping to stderr instead:");
                try {
                    DbxAuthInfo.Writer.writeToStream(authInfo, System.err);
                } catch (IOException ex1) {
                    Logger.getLogger(DropboxAuth.class.getName()).log(Level.SEVERE, null, ex1);
                }
                System.exit(1); return;
            }
        });
        
    }   
}
