package ExplorerControl;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

import java.io.File;

public class ExplorerControl {

    private File currentDir;

    public void init(ListView<File> explorer, File startDir) {
        currentDir = startDir;
        loadFiles(explorer, currentDir);
        setupNavigation(explorer);
    }

    public void loadFiles(ListView<File> explorer, File dir) {

        explorer.getItems().clear();

        File[] files = dir.listFiles();
        if (files == null) return;

        explorer.getItems().addAll(files);
    }

    public void setupNavigation(ListView<File> explorer) {

        explorer.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(File item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getName());
            }
        });

        explorer.setOnMouseClicked(e -> {
            if (e.getClickCount() != 2) return;

            File selected = explorer.getSelectionModel().getSelectedItem();
            if (selected == null) return;

            if (selected.isDirectory()) {
                currentDir = selected;
                loadFiles(explorer, currentDir);
            } else {
                System.out.println("Open file: " + selected.getAbsolutePath());
            }
        });
    }
}