package SearchController;

import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import SearchService.SearchService;
import java.io.File;
import java.util.List;
// import SearchService;
import FileInfo.FileInfo;
import FileOpener.FileOpener;

public class SearchController {

    private SearchService searchService = new SearchService();

    public void attachEvents(TextField searchBar, javafx.scene.control.Button searchBtn,
                             ListView<String> results) {

        searchBtn.setOnAction(e -> performSearch(searchBar, results));

        searchBar.setOnKeyReleased(e -> performSearch(searchBar, results));

        results.setOnMouseClicked(e -> handleDoubleClick(e, results));
    }

    private void handleDoubleClick(MouseEvent event, ListView<String> results) {

        if (event.getClickCount() != 2) return;

        String path = results.getSelectionModel().getSelectedItem();
        if (path == null) return;

        FileOpener.open(new File(path));
    }

    private void performSearch(TextField searchBar, ListView<String> results) {

        String query = searchBar.getText();

        results.getItems().clear();

        List<FileInfo> files = searchService.search(query);
        if (files == null) return;

        for (FileInfo f : files) {
            results.getItems().add(f.getPathName());
        }
    }
}