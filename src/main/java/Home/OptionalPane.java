package Home;

import Home.CloudTab.FMetadata;
import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class OptionalPane extends TabPane {
    private CloudTab ht;
    private TableView<CloudTab.FileInfo> searchResultTable;
    public boolean isSearching = false;
    public OptionalPane(CloudTab ht) {
        super();
        this.tabClosingPolicyProperty().set(TabClosingPolicy.ALL_TABS);
        Tab searchTab = new Tab("Search Result");
        this.setVisible(false);
        this.getStyleClass().add("op");
        this.ht = ht;
        
        searchResultTable = new TableView<>();
        TableColumn fileNameCol = new TableColumn("Name");
        TableColumn fileSizeCol = new TableColumn("Size");
        TableColumn fileDateCol = new TableColumn("Last Modified");
        fileNameCol.prefWidthProperty().bind(searchResultTable.widthProperty().multiply(0.5));
        fileNameCol.setMinWidth(100);
        fileNameCol.maxWidthProperty().bind(searchResultTable.widthProperty().subtract(fileSizeCol.minWidthProperty()).subtract(fileDateCol.minWidthProperty()));
        fileNameCol.setCellValueFactory(
                new PropertyValueFactory<>("fileName"));
 
        
        fileSizeCol.prefWidthProperty().bind(searchResultTable.widthProperty().subtract(fileNameCol.widthProperty()).multiply(0.4));
        fileSizeCol.setMinWidth(50);
        fileSizeCol.maxWidthProperty().bind(searchResultTable.widthProperty().subtract(fileNameCol.widthProperty()).subtract(fileDateCol.minWidthProperty()));
        fileSizeCol.setCellValueFactory(
                new PropertyValueFactory<>("fileSize"));
 
        
        fileDateCol.prefWidthProperty().bind(searchResultTable.widthProperty().subtract(fileNameCol.widthProperty()).subtract(fileSizeCol.widthProperty()));
        fileDateCol.setMinWidth(50);
        fileDateCol.setCellValueFactory(
                new PropertyValueFactory<>("lastModified"));

        searchResultTable.getColumns().addAll(fileNameCol, fileSizeCol, fileDateCol);
        searchTab.setContent(searchResultTable);
        this.getTabs().add(searchTab);
    }
    
    public void showOptionalPane(ObservableList<FMetadata> data) {
//        if (data.isEmpty()) {
//            return;
//        }
        
        searchResultTable.getItems().clear();

        data.forEach((md) -> {
            System.out.println(md.fileName);
            CloudTab.FileInfo ri = new CloudTab.FileInfo(md.cloud, md.fileName, md.fileSize, md.lastModified);
            searchResultTable.getItems().add(ri);
        });
        searchResultTable.setEditable(false);
        if(!isSearching) {
            ht.setPrefWidth(ht.getWidth()/2);
            isSearching = true;
        }
        
        this.setVisible(true);
    }
    
    public void hideOptionalPane() {
        this.setVisible(false);
        isSearching = false;
    }
}