/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Home;

import SignIn.SignInPane;
import java.util.Optional;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Pair;
/**
 *             <tabs>
                <Tab text="All">
                    <TableView fx:id="treeTableView">
                        <columns>
                            <TableColumn fx:id="name"
                                                minWidth="${homePane.width*0.2}" maxWidth="${homePane.width*0.85}" text="Name" prefWidth="${homePane.width*0.7}"
                                                resizable="true"></TableColumn>
                            <TableColumn fx:id="size" text="Size"
                                                minWidth="${homePane.width*0.075}" prefWidth="${(homePane.width-name.width)/2}"
                                                resizable="true">
                            </TableColumn>
                            <TableColumn fx:id="type" text="Type"
                                                minWidth="${homePane.width*0.075}" prefWidth="${(homePane.width-name.width)/2}"
                                                resizable="true">
                            </TableColumn>
                        </columns>
                    </TableView>
                </Tab>
                <Tab text="+" />
            </tabs>

 */
/**
 *
 * @author t_kimka
 */
public final class HomeTab extends TabPane {  
    private enum cloudType {
        dropbox,
        googleDrive,
        oneDrive
    }
    public HomeTab() {
        TableView table = this.getEmptyTable();
        Tab allTab = new Tab("All", table);
        Tab plusTab = new Tab("+");
        plusTab.setOnSelectionChanged(new EventHandler<Event>() {
            cloudType chosen;

            @Override
            public void handle(Event t) {
                Dialog<cloudType> dialog = new Dialog<>();
                dialog.setHeaderText("Supported Cloud Storage Lists");

                // Set the icon (must be included in the project).
                // dialog.setGraphic(new ImageView(this.getClass().getResource("login.png").toString()));

                // Set the button types.
                ButtonType okButton = new ButtonType("okButton", ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().addAll(okButton, ButtonType.CANCEL);

                // Create the username and password labels and fields.
                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(20, 150, 10, 10));

                ImageView dropboxIcon = new ImageView(this.getClass().getResource("/img/600DropboxIconWithName.png").toString());
                dropboxIcon.setFitHeight(40);
                dropboxIcon.setPreserveRatio(true);
                Button dropboxButton = new Button("", dropboxIcon);
                dropboxButton.setUserData(cloudType.dropbox);
                
                ImageView googleDriveIcon = new ImageView(this.getClass().getResource("/img/600GoogleDriveIconWithName.png").toString());
                googleDriveIcon.setFitHeight(40);
                googleDriveIcon.setPreserveRatio(true);
                Button googleDriveButton = new Button("", googleDriveIcon);
                googleDriveButton.setUserData(cloudType.googleDrive);

                ImageView oneDriveIcon = new ImageView(this.getClass().getResource("/img/600OneDriveWithName.png").toString());
                oneDriveIcon.setFitHeight(40);
                oneDriveIcon.setPreserveRatio(true);
                Button oneDriveButton = new Button("", oneDriveIcon);
                oneDriveButton.setUserData(cloudType.oneDrive);
                
                grid.add(dropboxButton, 0, 0);
                grid.add(googleDriveButton, 0, 1);
                grid.add(oneDriveButton, 0, 2);

                // Enable/Disable login button depending on whether a username was entered.
                Node okButtonNode = dialog.getDialogPane().lookupButton(okButton);
                okButtonNode.setDisable(true);

                // Do some validation (using the Java 8 lambda syntax).
                dropboxButton.focusedProperty().addListener((observable, wasFocused, focused) -> {
                    okButtonNode.setDisable(!focused);
                    this.chosen = (cloudType) dropboxButton.getUserData();                    
                });
                googleDriveButton.focusedProperty().addListener((observable, wasFocused, focused) -> {
                    okButtonNode.setDisable(!focused);     
                    this.chosen = (cloudType) googleDriveButton.getUserData();
                });
                oneDriveButton.focusedProperty().addListener((observable, wasFocused, focused) -> {
                    okButtonNode.setDisable(!focused);    
                    this.chosen = (cloudType) oneDriveButton.getUserData();
                });

                dialog.getDialogPane().setContent(grid);

                // Convert the result to a username-password-pair when the login button is clicked.
                dialog.setResultConverter(dialogButton -> {
                    if (dialogButton == okButton) {
                        return this.chosen;
                    }
                    return null;
                });

                Optional<cloudType> result = dialog.showAndWait();

                result.ifPresent(buttonData -> {
                    if(buttonData == cloudType.dropbox) {
                        Dialog dropboxPrompt = new Dialog();
                        WebView browser = new WebView();
                        WebEngine webEngine = browser.getEngine();
                        webEngine.load("https://www.dropbox.com/oauth2/authorize?client_id=dn1ejzxezzcblo0&response_type=code");
                        dropboxPrompt.getDialogPane().setExpandableContent(browser);
                        dropboxPrompt.showAndWait();
                    }
                    System.out.println("Username=" + buttonData);
                });
            }
        });
        this.getTabs().add(allTab);
        this.getTabs().add(plusTab);
    }

    public TableView getEmptyTable() {
        TableView<FileInfo> table = new TableView<>();
        ObservableList<FileInfo> data = FXCollections.observableArrayList();
        TableColumn fileNameCol = new TableColumn("Name");
        fileNameCol.setMinWidth(100);
        fileNameCol.setCellValueFactory(
                new PropertyValueFactory<>("fileName"));
 
        TableColumn fileSizeCol = new TableColumn("Size");
        fileSizeCol.setMinWidth(100);
        fileSizeCol.setCellValueFactory(
                new PropertyValueFactory<>("fileSize"));
 
        TableColumn fileTypeCol = new TableColumn("Type");
        fileTypeCol.setMinWidth(200);
        fileTypeCol.setCellValueFactory(
                new PropertyValueFactory<>("fileType"));
 
        table.setItems(data);
        table.getColumns().addAll(fileNameCol, fileSizeCol, fileTypeCol);

        table.setEditable(false);
        
        return table;
    }
    public static class FileInfo {
 
        private final SimpleStringProperty fileName;
        private final SimpleIntegerProperty fileSize;
        private final SimpleStringProperty fileType;
 
        private FileInfo(String fName, int lName, String email) {
            this.fileName = new SimpleStringProperty(fName);
            this.fileSize = new SimpleIntegerProperty(lName);
            this.fileType = new SimpleStringProperty(email);
        }
 
        public String getFileName() {
            return fileName.get();
        }
 
        public void setFileName(String fName) {
            fileName.set(fName);
        }
 
        public int getFileSize() {
            return fileSize.get();
        }
 
        public void setFileSize(int fSize) {
            fileSize.set(fSize);
        }
 
        public String getFileType() {
            return fileType.get();
        }
 
        public void setFileType(String fType) {
            fileType.set(fType);
        }
    }
}