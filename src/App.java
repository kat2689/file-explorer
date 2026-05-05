import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;

import UI.UI;
import SearchController.SearchController;
import ExplorerControl.ExplorerControl;

public class App extends Application {

    private ListView<String> results;
    private ListView<File> explorer;

    private final UI ui = new UI();
    private final SearchController searchController = new SearchController();
    private final ExplorerControl explorerControl = new ExplorerControl();

    @Override
    public void start(Stage stage) {

        TextField searchBar = ui.createSearchBar();
        Button searchBtn = ui.createSearchButton();

        results = new ListView<>();
        explorer = new ListView<>();

        File startDir = new File("C:\\MY FOLDER\\Rishi Folder\\Personal Projects");

        explorerControl.init(explorer, startDir);
        searchController.attachEvents(searchBar, searchBtn, results);

        VBox root = new VBox(10,
                new Label("Search"),
                searchBar, searchBtn, results,
                new Label("Explorer"),
                explorer
        );

        stage.setTitle("Fast File Search");
        stage.setScene(new Scene(root, 500, 400));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}