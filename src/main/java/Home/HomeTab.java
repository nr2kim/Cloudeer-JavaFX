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
import com.dropbox.core.v2.files.MediaInfo;
import java.util.Optional;
import java.util.SortedMap;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private DropboxAuth dropboxAuth;
    private ObservableList<FMetadata> allTable;
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
        allTable = FXCollections.observableArrayList();
        TableView table = this.getTable(allTable);
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
                            this.dropboxAuth = new DropboxAuth();
                            Optional<String> authenticationResult = dropboxAuth.showAndWait();
                            authenticationResult.ifPresent((String accessToken) -> {
                                try {
                                    if(dropboxAuth.onAuthenticated(accessToken)) {
                                        addTab(cloudType.dropbox, dropboxAuth.getAuthInfo(), dropboxAuth.getFiles(""));
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
    
    public void addTab(cloudType type, DbxAuthInfo authInfo, ObservableList<FMetadata> data) {
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

    public TableView getTable(ObservableList<FMetadata> data) {
        TableView<FileInfo> table = new TableView<>();
        TableColumn fileNameCol = new TableColumn("Name");
        TableColumn fileSizeCol = new TableColumn("Size");
        TableColumn fileDateCol = new TableColumn("Last Modified");
        
        fileNameCol.prefWidthProperty().bind(table.widthProperty().multiply(0.5));
        fileNameCol.setMinWidth(300);
        fileNameCol.maxWidthProperty().bind(table.widthProperty().subtract(fileSizeCol.minWidthProperty()).subtract(fileDateCol.minWidthProperty()));
        fileNameCol.setCellValueFactory(
                new PropertyValueFactory<>("fileName"));
 
        
        fileSizeCol.prefWidthProperty().bind(table.widthProperty().subtract(fileNameCol.widthProperty()).multiply(0.4));
        fileSizeCol.setMinWidth(100);
        fileSizeCol.maxWidthProperty().bind(table.widthProperty().subtract(fileNameCol.widthProperty()).subtract(fileDateCol.minWidthProperty()));
        fileSizeCol.setCellValueFactory(
                new PropertyValueFactory<>("fileSize"));
 
        
        fileDateCol.prefWidthProperty().bind(table.widthProperty().subtract(fileNameCol.widthProperty()).subtract(fileSizeCol.widthProperty()));
        fileDateCol.setMinWidth(200);
        fileDateCol.setCellValueFactory(
                new PropertyValueFactory<>("lastModified"));

        table.getColumns().addAll(fileNameCol, fileSizeCol, fileDateCol);

        data.stream().map((md) -> new FileInfo(md.fileName, md.fileSize, md.lastModified)).forEachOrdered((ri) -> {
            System.out.println(ri.getFileName() + " " + ri.getFileSize());
            table.getItems().add(ri);
        });
        
        table.getSelectionModel().selectedIndexProperty().addListener((obj, oldSelection, newSelection) -> {
            if (data.get((int) newSelection).isFolder) {
                ObservableList<FMetadata> allSubFiles = dropboxAuth.getFiles(data.get((int) newSelection).fullPath);
                System.out.println(allSubFiles.get(0).fileName);
            }
        });

        table.setEditable(false);
        
        return table;
    }
    
    public static class FMetadata {
        public final String fileName;
        public final String fileSize;
        public final String lastModified;
        public final Boolean isFolder;
        public final MediaInfo isMedia;
        public final String fullPath;
        
        public FMetadata(String fName, String fsize, String lModified, Boolean isF, MediaInfo isM, String fPath) {
            this.fileName = fName;
            this.fileSize = fsize;
            this.lastModified = lModified;
            this.isFolder = isF;
            this.isMedia = isM;
            this.fullPath = fPath;
        }
    }
    
    public static class FileInfo {
        private final SimpleStringProperty fileName;
        private final SimpleStringProperty fileSize;
        private final SimpleStringProperty lastModified;
        
        public FileInfo(String fName, String fsize, String lModified) {
            this.fileName = new SimpleStringProperty(fName);
            this.fileSize = new SimpleStringProperty(fsize);
            this.lastModified = new SimpleStringProperty(lModified);
        }
        public String getFileName() {
            return this.fileName.get();
        }
 
        public void setFileName(String fName) {
            this.fileName.set(fName);
        }
 
        public String getFileSize() {
            return this.fileSize.get();
        }
 
        public void setFileSize(String fSize) {
            this.fileSize.set(fSize);
        }
 
        public String getFileType() {
            return this.lastModified.get();
        }
 
        public void setLastModified(String lModified) {
            this.lastModified.set(lModified);
        }
    }
}