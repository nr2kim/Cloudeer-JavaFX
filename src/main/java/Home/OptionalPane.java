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
        
        TableView<HomeTab.FileInfo> table = new TableView<>();
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
        this.getChildren().add(table);
    }
    
    public void showOptionalPane(ObservableList<FMetadata> data) {
        if (data.isEmpty()) {
            return;
        }
        data.forEach((md) -> {
            HomeTab.FileInfo ri = new HomeTab.FileInfo(md.cloud, md.fileName, md.fileSize, md.lastModified);
            searchResultTable.getItems().add(ri);
        });
        searchResultTable.setOnMousePressed(new EventHandler<MouseEvent>() {
            EventTarget previousTarget = null;
            Date lastClickTime = new Date();
            @Override 
            public void handle(MouseEvent event) {
                Date now = new Date();
                long diff = now.getTime() - lastClickTime.getTime();
                if (event.isPrimaryButtonDown() && previousTarget == event.getTarget() && diff < 300) {
                    int rowIndex = searchResultTable.getSelectionModel().getSelectedIndex();
                    if (data.get(rowIndex).children != null) {
                        ObservableList<HomeTab.FMetadata> allSubFiles = data.get(rowIndex).children;
                        showOptionalPane(allSubFiles);
                    }
                } else if (event.isPrimaryButtonDown()) {
                    previousTarget = event.getTarget();
                    lastClickTime = new Date();
                }
            }
        });
        searchResultTable.setEditable(false);
        this.setPrefSize(ht.getWidth()/2, ht.getHeight()/2);
        ht.setPrefSize(ht.getWidth()/2, ht.getHeight()/2);
        
        this.setVisible(true);
    }
    
    public void hideOptionalPane() {
        ht.setPrefSize(ht.getWidth()*2, ht.getHeight()*2);
        this.setVisible(false);
    }
}