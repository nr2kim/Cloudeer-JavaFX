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
import java.util.Date;
import java.util.Optional;
import java.util.SortedMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.scene.control.Dialog;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author t_kimka
 */
public final class HomeTab extends TabPane {
    public SortedMap<cloudType, DbxAuthInfo> allClouds;
    public ObservableList<cloudType> tabsCloud;
    private DropboxAuth dropboxAuth;
    public ObservableList<FMetadata> allTabData;
    public static ObservableList<ObservableList<FMetadata>> allMetadata;
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
        this.setPrefHeight(Home.homeScreen.getHeight() - 40);
        this.setPrefWidth(Home.homeScreen.getWidth());
        this.tabClosingPolicyProperty().set(TabClosingPolicy.UNAVAILABLE);
        this.allTabData = FXCollections.observableArrayList();
        this.tabsCloud = FXCollections.observableArrayList();
        allMetadata = FXCollections.observableArrayList();
        Tab allTab = new Tab("All");
        this.setTable(allTab, this.allTabData);

        Tab plusTab = new Tab("+");
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
                                        addTab(cloudType.dropbox, dropboxAuth.getFiles(""));
                                    }
                                } catch (DbxException ex) {
                                    Logger.getLogger(DropboxAuth.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            });
                            break;
                        case googleDrive:
                            addTab(cloudType.googleDrive, null);
                            break;
                        case oneDrive:
                            addTab(cloudType.oneDrive, null);
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
    }
    
    public void addTab(cloudType type, ObservableList<FMetadata> data) {
        Tab newTab = new Tab("");
        this.tabsCloud.add(type);
        newTab.setClosable(true);
        ImageView img = new ImageView(type.toString());
        img.setFitHeight(15);
        img.setPreserveRatio(true);
        newTab.setGraphic(img);
        this.setTable(newTab, data);
        this.getTabs().add(tabsCloud.size(), newTab);
        
        // Update allTable
        this.allTabData.addAll(data);
        allMetadata.addAll(data);
        this.setTable(this.getTabs().get(0), this.allTabData);
        
        this.getSelectionModel().select(newTab);
    }

    public void setTable(Tab tab, ObservableList<FMetadata> data) {
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

        data.forEach((md) -> {
            FileInfo ri = new FileInfo(md.cloud, md.fileName, md.fileSize, md.lastModified);
            table.getItems().add(ri);
        });
        table.setOnMousePressed(new EventHandler<MouseEvent>() {
            EventTarget previousTarget = null;
            Date lastClickTime = new Date();
            @Override 
            public void handle(MouseEvent event) {
                Date now = new Date();
                long diff = now.getTime() - lastClickTime.getTime();
                if (event.isPrimaryButtonDown() && previousTarget == event.getTarget() && diff < 300) {
                    int rowIndex = table.getSelectionModel().getSelectedIndex();
                    if (data.get(rowIndex).children != null) {
                        ObservableList<FMetadata> allSubFiles = data.get(rowIndex).children;
                        setTable(tab, allSubFiles);
                    }
                } else if (event.isPrimaryButtonDown()) {
                    previousTarget = event.getTarget();
                    lastClickTime = new Date();
                }
            }
        });
        table.setEditable(false);
        tab.setContent(table);
    }
    
    public static class FMetadata {
        public final cloudType cloud;
        public final String fileName;
        public final String fileSize;
        public final String lastModified;
        public final ObservableList<FMetadata> children;
        public final MediaInfo isMedia;
        public final String fullPath;
        
        public FMetadata(cloudType ct, String fName, String fsize, String lModified, ObservableList<FMetadata> cList, MediaInfo isM, String fPath) {
            this.cloud = ct;
            this.fileName = fName;
            this.fileSize = fsize;
            this.lastModified = lModified;
            this.children = cList;
            this.isMedia = isM;
            this.fullPath = fPath;
        }
    }
    
    public static class FileInfo {
        // All are properties for binding purposes (observables)
        private final SimpleObjectProperty<cloudType> cloud;
        private final SimpleStringProperty fileName;
        private final SimpleStringProperty fileSize;
        private final SimpleStringProperty lastModified;
        
        public FileInfo(cloudType ct, String fName, String fsize, String lModified) {
            this.cloud = new SimpleObjectProperty(ct);
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