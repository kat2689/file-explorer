package ModernUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.Node;
import java.io.File;

/**
 * DirectoryTreeItem — A lazy-loading TreeItem for the directory tree.
 * Children are only loaded when the node is first expanded, which provides
 * excellent performance even on large file systems.
 * 
 * This class uses only java.io.File — no backend modification needed.
 */
public class DirectoryTreeItem extends TreeItem<String> {

    private final File file;
    private boolean childrenLoaded = false;
    private boolean isLeafNode;
    private boolean leafComputed = false;

    /**
     * Creates a new DirectoryTreeItem for the given directory.
     *
     * @param file The directory this tree node represents
     */
    public DirectoryTreeItem(File file) {
        super(file.getAbsolutePath().endsWith("\\") ? file.getAbsolutePath() : file.getName());
        this.file = file;

        // Set appropriate icon
        if (isSystemRoot(file)) {
            setGraphic(IconHelper.driveIcon(16));
        } else {
            setGraphic(IconHelper.folderIcon(16));
        }

        // Listen for expand to trigger lazy loading
        expandedProperty().addListener((obs, wasExpanded, isNowExpanded) -> {
            if (isNowExpanded && !childrenLoaded) {
                loadChildren();
            }
            // Update icon based on expand state
            if (!isSystemRoot(file)) {
                setGraphic(IconHelper.folderIcon(16));
            }
        });
    }

    /**
     * Returns the File object this tree item represents.
     */
    public File getFile() {
        return file;
    }

    /**
     * Overridden to determine if this node is a leaf (has no subdirectories).
     */
    @Override
    public boolean isLeaf() {
        if (!leafComputed) {
            leafComputed = true;
            if (file.isDirectory()) {
                File[] children = file.listFiles();
                if (children != null) {
                    for (File child : children) {
                        if (child.isDirectory() && !child.isHidden()) {
                            isLeafNode = false;
                            return false;
                        }
                    }
                }
                isLeafNode = true;
            } else {
                isLeafNode = true;
            }
        }
        return isLeafNode;
    }

    /**
     * Overridden to provide lazy-loaded children.
     */
    @Override
    public ObservableList<TreeItem<String>> getChildren() {
        if (!childrenLoaded) {
            // Add a placeholder so the expand arrow appears
            // Actual loading happens on expand via the listener
            ObservableList<TreeItem<String>> superChildren = super.getChildren();
            if (superChildren.isEmpty() && !isLeaf()) {
                superChildren.add(new TreeItem<>("Loading..."));
            }
            return superChildren;
        }
        return super.getChildren();
    }

    /**
     * Loads subdirectory children on demand.
     */
    private void loadChildren() {
        childrenLoaded = true;
        ObservableList<TreeItem<String>> children = FXCollections.observableArrayList();

        File[] files = file.listFiles();
        if (files != null) {
            // Sort: directories first, then alphabetical
            java.util.Arrays.sort(files, (a, b) -> {
                if (a.isDirectory() && !b.isDirectory()) return -1;
                if (!a.isDirectory() && b.isDirectory()) return 1;
                return a.getName().compareToIgnoreCase(b.getName());
            });

            for (File childFile : files) {
                // Only show directories in the tree, skip hidden files
                if (childFile.isDirectory() && !childFile.isHidden()) {
                    children.add(new DirectoryTreeItem(childFile));
                }
            }
        }

        super.getChildren().setAll(children);
    }

    /**
     * Forces a refresh of this node's children.
     */
    public void refresh() {
        childrenLoaded = false;
        leafComputed = false;
        loadChildren();
    }

    /**
     * Checks if a file represents a system root drive (e.g., C:\).
     */
    private static boolean isSystemRoot(File file) {
        File[] roots = File.listRoots();
        if (roots != null) {
            for (File root : roots) {
                if (root.equals(file)) return true;
            }
        }
        return false;
    }
}
