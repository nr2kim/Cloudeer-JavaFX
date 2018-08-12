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
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

/**
 *
 * @author t_kimka
 */
public final class HomeTab extends TabPane {
    public SortedMap<cloudType, DbxAuthInfo> allClouds;
    public int numTabs;
    private DropboxAuth dropboxAuth;
    private ObservableList<ObservableList<FMetadata>> allMetadata;
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
        allMetadata = FXCollections.observableArrayList();
        this.allMetadata.add(0, FXCollections.observableArrayList());
        TableView allTable = this.getTable(this.allMetadata.get(0));
        
        Tab allTab = new Tab("All", allTable);
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
        table.setRowFactory(tv -> {
            TableRow<FileInfo> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) && data.get(row.getIndex()).isFolder ) {
                    ObservableList<FMetadata> allSubFiles = dropboxAuth.getFiles(data.get(row.getIndex()).fullPath);
                    TableView subTable = this.getTable(allSubFiles);
                    FileInfo toParentDir = new FileInfo("...", "", "");
                    subTable.setRowFactory(subt -> {
                        TableRow<FileInfo> subr = new TableRow<>();
                        subr.setOnMouseClicked(subrEvent -> {
                            if(subr.getIndex() == 0) {
                                newTab.setContent(table);
                            }
                        });
                        
                        return subr;
                    });
                    subTable.getItems().add(0, toParentDir);
                    newTab.setContent(subTable);
                }
            });
            return row ;
        });
        newTab.setClosable(true);
        ImageView img = new ImageView(type.toString());
        img.setFitHeight(15);
        img.setPreserveRatio(true);
        newTab.setGraphic(img);
//        allClouds.put(type, authInfo);
        this.getTabs().add(numTabs, newTab);
        this.allMetadata.add(numTabs, data);
        
        // Update allTable
        this.allMetadata.get(0).addAll(data);
        TableView allTable = this.getTable(this.allMetadata.get(0));
        this.getTabs().get(0).setContent(allTable);
        
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
            table.getItems().add(ri);
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