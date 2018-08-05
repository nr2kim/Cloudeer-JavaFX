/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Home;

import Auth.DropboxAuth;
import SignIn.SignInDialog;
import com.dropbox.core.DbxAuthFinish;
import java.io.IOException;
import java.util.Optional;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Dialog;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

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
    public enum cloudType {
        dropbox,
        googleDrive,
        oneDrive
    }
    public HomeTab() {
        TableView table = this.getEmptyTable();
        Tab allTab = new Tab("All", table);
        Tab plusTab = new Tab("+");
        plusTab.setOnSelectionChanged(new EventHandler<Event>() {
            @Override
            public void handle(Event t) {
                Dialog dialog = new SignInDialog();
                Optional<cloudType> cloudResult = dialog.showAndWait();

                cloudResult.ifPresent(buttonData -> {
                    if(buttonData == cloudType.dropbox) {
                        Dialog dropboxAuth = new DropboxAuth();
                        
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