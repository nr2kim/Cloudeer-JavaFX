package SignIn;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Home.HomeTab;
import Home.HomeTab.FileInfo;
import Home.HomeTab.cloudType;
import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxAuthInfo;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuth;
import com.dropbox.core.json.JsonReader;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.users.FullAccount;
import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    private DbxWebAuth webAuth;
    private DbxAppInfo appInfo;
    private DbxRequestConfig config;
    private DbxAuthInfo authInfo;
    private DbxAuthFinish authFinish;
    private DbxClientV2 client;
    public DropboxAuth() {
        
        
        
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

        
        
    }
    
    public ObservableList<FileInfo> getFiles() {
        ObservableList<FileInfo> data = FXCollections.observableArrayList();
        // Get current account info
        FullAccount account = null;
        try {
            account = client.users().getCurrentAccount();
        } catch (DbxException ex) {
            Logger.getLogger(DropboxAuth.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
        System.out.println(account.getName().getDisplayName());

        // Get files and folder metadata from Dropbox root directory
        ListFolderResult result = null;
        try {
            result = client.files().listFolder("");
        } catch (DbxException ex) {
            Logger.getLogger(DropboxAuth.class.getName()).log(Level.SEVERE, null, ex);
        }
        while (true) {
            for (Metadata metadata : result.getEntries()) {
                System.out.println(metadata.getPathLower());
                
                data.add(new FileInfo(metadata.getPathLower(), 0, ""));
            }

            if (!result.getHasMore()) {
                break;
            }

            try {
                result = client.files().listFolderContinue(result.getCursor());
                
            } catch (DbxException ex) {
                Logger.getLogger(DropboxAuth.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        // Upload "test.txt" to Dropbox
//        try (InputStream in = new FileInputStream("test.txt")) {
//            FileMetadata metadata = client.files().uploadBuilder("/test.txt")
//                .uploadAndFinish(in);
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(DropboxAuth.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(DropboxAuth.class.getName()).log(Level.SEVERE, null, ex);
//        }

        return data;
    }
    
    public boolean onAuthenticated(String accessToken) throws DbxException {
        
            
        System.out.println("AccessToken=" + accessToken);

        try {
            authFinish = webAuth.finishFromCode(accessToken);
        } catch (DbxException ex) {
            Dialog errDialog = new Dialog();
            errDialog.setContentText("Error in DbxWebAuth.authorize: " + ex.getMessage() 
                    + "\n Please try again with the correct access token.");
            ButtonType okButton = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
            errDialog.getDialogPane().getButtonTypes().add(okButton);
            Optional<ButtonType> err = errDialog.showAndWait();
            if (err.get() == ButtonType.OK){
                return false;
            }
        }

        System.out.println("Authorization complete.");
        System.out.println("- User ID: " + authFinish.getUserId());
        System.out.println("- Account ID: " + authFinish.getAccountId());
        System.out.println("- Access Token: " + authFinish.getAccessToken());

        // Save auth information to output file.
        this.authInfo = new DbxAuthInfo(authFinish.getAccessToken(), appInfo.getHost());
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
            return false;
        }
        this.client = new DbxClientV2(config, authFinish.getAccessToken());
        return true;
    }
    
    public DbxAuthInfo getAuthInfo() {
        return this.authInfo;
    }
}
