import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

import FileOpener.FileOpener;
import FileInfo.FileInfo;

public class App extends Application {

    private final SearchService searchService = new SearchService();

    @Override
    public void start(Stage stage) {

        TextField searchBar = createSearchBar();
        Button searchBtn = createSearchButton();
        ListView<String> results = createResultsList();

        attachEvents(searchBar, searchBtn, results);

        VBox root = new VBox(10, searchBar, searchBtn, results);

        stage.setTitle("Fast File Search");
        stage.setScene(new Scene(root, 500, 400));
        stage.show();
    }

    // ---------------- UI ----------------

    private TextField createSearchBar() {
        TextField searchBar = new TextField();
        searchBar.setPromptText("Search files...");
        return searchBar;
    }

    private Button createSearchButton() {
        return new Button("Search");
    }

    private ListView<String> createResultsList() {
        ListView<String> results = new ListView<>();

        results.setCellFactory(param -> new ListCell<String>() {
            @Override
            protected void updateItem(String path, boolean empty) {
                super.updateItem(path, empty);

                if (empty || path == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setGraphic(createFileCell(path));
                }
            }
        });

        return results;
    }

    private VBox createFileCell(String path) {
        File file = new File(path);

        javafx.scene.control.Label name =
                new javafx.scene.control.Label(file.getName());
        name.setStyle("-fx-font-weight: bold;");

        javafx.scene.control.Label folder =
                new javafx.scene.control.Label(file.getParent());
        folder.setStyle("-fx-text-fill: gray; -fx-font-size: 11;");

        return new VBox(name, folder);
    }


    private void attachEvents(TextField searchBar, Button searchBtn, ListView<String> results) {

        searchBtn.setOnAction(e -> performSearch(searchBar, results));

        searchBar.setOnKeyReleased(e -> performSearch(searchBar, results));

        results.setOnMouseClicked(event -> handleDoubleClick(event, results));
    }

    private void handleDoubleClick(javafx.scene.input.MouseEvent event, ListView<String> results) {

        if (event.getClickCount() != 2) return;

        String path = results.getSelectionModel().getSelectedItem();
        System.out.println("Clicked: " + path);

        if (path == null) return;

        FileOpener.open(new File(path));
    }



    private void performSearch(TextField searchBar, ListView<String> results) {

        String query = searchBar.getText();

        results.getItems().clear();

        List<FileInfo> files = searchService.search(query);

        if (files != null) {
            for (FileInfo f : files) {
                results.getItems().add(f.getPathName());
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}