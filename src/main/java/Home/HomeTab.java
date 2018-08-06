/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Home;

import SignIn.DropboxAuth;
import SignIn.SignInDialog;
import com.dropbox.core.DbxAuthInfo;
import com.dropbox.core.DbxException;
import java.util.Optional;
import java.util.SortedMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Dialog;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

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
    public SortedMap<cloudType, DbxAuthInfo> allClouds;
    public int numTabs;
    public enum cloudType {
        dropbox {
            @Override
            public String toString() {
                return this.getClass().getResource("/img/600DropboxIconWithName.png").toString();
            }
        },

        googleDrive {
            @Override
            public String toString() {
                return this.getClass().getResource("/img/600GoogleDriveIconWithName.png").toString();
            }
        },
        
        oneDrive {
            @Override
            public String toString() {
                return this.getClass().getResource("/img/600OneDriveWithName.png").toString();
            }
        }
    }
    public HomeTab() {
        this.setTabClosingPolicy(TabClosingPolicy.ALL_TABS);
        TableView table = this.getTable(FXCollections.observableArrayList());
        Tab allTab = new Tab("All", table);
        allTab.setClosable(false);
        Tab plusTab = new Tab("+");
        plusTab.setClosable(false);
        plusTab.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
            if(isNowSelected) {
                this.getSelectionModel().selectFirst();
                Dialog dialog = new SignInDialog();
                Optional<cloudType> cloudResult = dialog.showAndWait();

                cloudResult.ifPresent((cloudType buttonData) -> {
                    switch (buttonData) {
                        case dropbox:
                            DropboxAuth dropboxAuth = new DropboxAuth();
                            Optional<String> authenticationResult = dropboxAuth.showAndWait();
                            authenticationResult.ifPresent((String accessToken) -> {
                                try {
                                    if(dropboxAuth.onAuthenticated(accessToken)) {
                                        addTab(cloudType.dropbox, dropboxAuth.getAuthInfo(), dropboxAuth.getFiles());
                                    }
                                } catch (DbxException ex) {
                                    Logger.getLogger(DropboxAuth.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            });
                            break;
                        case googleDrive:
                            addTab(cloudType.googleDrive, null, null);
                            break;
                        case oneDrive:
                            addTab(cloudType.oneDrive, null, null);
                            break;
                        default:
                            Logger.getLogger(HomeTab.class.getName()).log(Level.SEVERE, null, buttonData);
                            System.exit(1);
                            break;
                    }
                });
            }   
        });
        this.getTabs().add(allTab);
        this.getTabs().add(plusTab);
        this.numTabs = 1;
    }
    
    public void addTab(cloudType type, DbxAuthInfo authInfo, ObservableList<FileInfo> data) {
        TableView table = this.getTable(data);
        Tab newTab = new Tab("", table);
        newTab.setClosable(true);
        ImageView img = new ImageView(type.toString());
        img.setFitHeight(15);
        img.setPreserveRatio(true);
        newTab.setGraphic(img);
//        allClouds.put(type, authInfo);
        this.getTabs().add(numTabs, newTab);
        this.getSelectionModel().select(newTab);

        numTabs++;
    }

    public TableView getTable(ObservableList data) {
        TableView<FileInfo> table = new TableView<>();
//        ObservableList<FileInfo> data = FXCollections.observableArrayList();
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
 
        public FileInfo(String fName, int lName, String email) {
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