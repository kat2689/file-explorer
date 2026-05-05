import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;

import ModernUI.ModernExplorerApp;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        // Use the same hardcoded start directory as the original
        File startDir = new File("C:\\MY FOLDER\\Rishi Folder\\Personal Projects");

        // Launch the modern File Explorer UI
        ModernExplorerApp modernApp = new ModernExplorerApp(startDir);
        modernApp.start(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}