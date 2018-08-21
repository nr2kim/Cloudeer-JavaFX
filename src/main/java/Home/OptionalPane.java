/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Home;

import Home.HomeTab.FMetadata;
import java.util.Date;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

/**
 *
 * @author t_kimka
 */
public class OptionalPane extends Pane {
    private HomeTab ht;
    private TableView<HomeTab.FileInfo> searchResultTable;

    public OptionalPane(HomeTab ht) {
        super();
        this.setVisible(false);
        this.ht = ht;
        
        searchResultTable = new TableView<>();
        TableColumn fileNameCol = new TableColumn("Name");
        TableColumn fileSizeCol = new TableColumn("Size");
        TableColumn fileDateCol = new TableColumn("Last Modified");
        fileNameCol.prefWidthProperty().bind(searchResultTable.widthProperty().multiply(0.5));
        fileNameCol.setMinWidth(300);
        fileNameCol.maxWidthProperty().bind(searchResultTable.widthProperty().subtract(fileSizeCol.minWidthProperty()).subtract(fileDateCol.minWidthProperty()));
        fileNameCol.setCellValueFactory(
                new PropertyValueFactory<>("fileName"));
 
        
        fileSizeCol.prefWidthProperty().bind(searchResultTable.widthProperty().subtract(fileNameCol.widthProperty()).multiply(0.4));
        fileSizeCol.setMinWidth(100);
        fileSizeCol.maxWidthProperty().bind(searchResultTable.widthProperty().subtract(fileNameCol.widthProperty()).subtract(fileDateCol.minWidthProperty()));
        fileSizeCol.setCellValueFactory(
                new PropertyValueFactory<>("fileSize"));
 
        
        fileDateCol.prefWidthProperty().bind(searchResultTable.widthProperty().subtract(fileNameCol.widthProperty()).subtract(fileSizeCol.widthProperty()));
        fileDateCol.setMinWidth(200);
        fileDateCol.setCellValueFactory(
                new PropertyValueFactory<>("lastModified"));

        searchResultTable.getColumns().addAll(fileNameCol, fileSizeCol, fileDateCol);
        this.getChildren().add(searchResultTable);
    }
    
    public void showOptionalPane(ObservableList<FMetadata> data) {
        if (data.isEmpty()) {
            return;
        }
        
        searchResultTable.getItems().clear();

        data.forEach((md) -> {
            System.out.println(md.fileName);
            HomeTab.FileInfo ri = new HomeTab.FileInfo(md.cloud, md.fileName, md.fileSize, md.lastModified);
            searchResultTable.getItems().add(ri);
        });
        searchResultTable.setEditable(false);
        if(this.isVisible() == false) {
            this.setPrefSize(ht.getWidth()/2, ht.getHeight());
            ht.setPrefWidth(ht.getWidth()/2);
            this.setLayoutX(ht.getWidth()/2);     
        }
        
        this.setVisible(true);
    }
    
    public void hideOptionalPane() {
        ht.setPrefSize(ht.getWidth()*2, ht.getHeight()*2);
        this.setVisible(false);
    }
}