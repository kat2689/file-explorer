import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

import FileInfo.FileInfo;

public class App extends Application {

    private final SearchService searchService = new SearchService();

    @Override
    public void start(Stage stage) {

        TextField searchBar = new TextField();
        searchBar.setPromptText("Search files...");

        Button searchBtn = new Button("Search");

        ListView<String> results = new ListView<>();

        searchBtn.setOnAction(e -> {

            String query = searchBar.getText();

            results.getItems().clear();

            List<FileInfo> files = searchService.search(query);

            if (files != null) {
                for (FileInfo f : files) {
                    results.getItems().add(f.getPathName());
                }
            }
        });

        VBox root = new VBox(10, searchBar, searchBtn, results);

        Scene scene = new Scene(root, 500, 400);

        stage.setTitle("File Explorer");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}