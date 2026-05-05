package ModernUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.Node;

import java.io.File;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import FileInfo.FileInfo;
import FileOpener.FileOpener;

/**
 * ModernExplorerController — Handles all UI events and state management.
 *
 * Responsibilities:
 *   - Navigation history (back/forward stacks)
 *   - TreeView click → load directory into TableView
 *   - Double-click file → delegate to FileOpener (existing backend)
 *   - Double-click folder → navigate into it
 *   - Context menu stubs (Open, Rename, Delete, Properties)
 *   - Address bar navigation
 *   - Search integration via SearchAdapter
 *   - Status bar updates
 *
 * This class does NOT modify any backend code.
 */
public class ModernExplorerController {

    // --- UI References ---
    private TreeView<String> treeView;
    private TableView<FileTableRow> tableView;
    private TextField addressBar;
    private TextField searchField;
    private Label statusItemCount;
    private Label statusSelection;
    private Button backBtn;
    private Button forwardBtn;

    // --- State ---
    private File currentDirectory;
    private final Deque<File> backStack = new ArrayDeque<>();
    private final Deque<File> forwardStack = new ArrayDeque<>();
    private boolean navigating = false; // prevents recursive history push

    // --- Backend Adapter ---
    private final SearchAdapter searchAdapter = new SearchAdapter();

    /**
     * Initializes the controller with all UI component references.
     */
    public void init(TreeView<String> treeView,
                     TableView<FileTableRow> tableView,
                     TextField addressBar,
                     TextField searchField,
                     Label statusItemCount,
                     Label statusSelection,
                     Button backBtn,
                     Button forwardBtn) {

        this.treeView = treeView;
        this.tableView = tableView;
        this.addressBar = addressBar;
        this.searchField = searchField;
        this.statusItemCount = statusItemCount;
        this.statusSelection = statusSelection;
        this.backBtn = backBtn;
        this.forwardBtn = forwardBtn;

        setupTreeViewEvents();
        setupTableViewEvents();
        setupAddressBarEvents();
        setupSearchEvents();
        setupTableSelection();
    }

    // ==========================================
    //  NAVIGATION
    // ==========================================

    /**
     * Navigates to a directory — loads its contents into the TableView,
     * updates the address bar, and manages history.
     */
    public void navigateTo(File directory) {
        if (directory == null || !directory.isDirectory()) return;

        // Push current directory to back stack (if not a history-based nav)
        if (!navigating && currentDirectory != null) {
            backStack.push(currentDirectory);
            forwardStack.clear();
        }

        currentDirectory = directory;

        // Update address bar
        addressBar.setText(directory.getAbsolutePath());

        // Load files into table
        loadFilesIntoTable(directory);

        // Select matching tree node (best-effort, don't expand entire tree)
        selectTreeNode(directory);

        // Update nav button states
        updateNavButtons();

        // Update status bar
        updateStatusBar();
    }

    /**
     * Goes back to the previous directory.
     */
    public void goBack() {
        if (backStack.isEmpty()) return;
        navigating = true;
        forwardStack.push(currentDirectory);
        File prev = backStack.pop();
        navigateTo(prev);
        navigating = false;
        updateNavButtons();
    }

    /**
     * Goes forward to the next directory.
     */
    public void goForward() {
        if (forwardStack.isEmpty()) return;
        navigating = true;
        backStack.push(currentDirectory);
        File next = forwardStack.pop();
        navigateTo(next);
        navigating = false;
        updateNavButtons();
    }

    /**
     * Navigates up one level to the parent directory.
     */
    public void goUp() {
        if (currentDirectory == null) return;
        File parent = currentDirectory.getParentFile();
        if (parent != null && parent.isDirectory()) {
            navigateTo(parent);
        }
    }

    /**
     * Refreshes the current directory listing.
     */
    public void refresh() {
        if (currentDirectory != null) {
            loadFilesIntoTable(currentDirectory);
            updateStatusBar();

            // Also refresh the tree node if it's a DirectoryTreeItem
            TreeItem<String> selected = treeView.getSelectionModel().getSelectedItem();
            if (selected instanceof DirectoryTreeItem) {
                ((DirectoryTreeItem) selected).refresh();
            }
        }
    }

    // ==========================================
    //  FILE LOADING
    // ==========================================

    /**
     * Loads files from a directory into the TableView.
     */
    private void loadFilesIntoTable(File directory) {
        ObservableList<FileTableRow> items = FXCollections.observableArrayList();

        File[] files = directory.listFiles();
        if (files != null) {
            // Sort: directories first, then alphabetical
            java.util.Arrays.sort(files, (a, b) -> {
                if (a.isDirectory() && !b.isDirectory()) return -1;
                if (!a.isDirectory() && b.isDirectory()) return 1;
                return a.getName().compareToIgnoreCase(b.getName());
            });

            for (File file : files) {
                // Skip hidden files
                if (!file.isHidden()) {
                    items.add(new FileTableRow(file));
                }
            }
        }

        tableView.setItems(items);
    }

    // ==========================================
    //  EVENT SETUP
    // ==========================================

    /**
     * Handles TreeView selection — clicking a folder loads it in the table.
     */
    private void setupTreeViewEvents() {
        treeView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal instanceof DirectoryTreeItem) {
                File dir = ((DirectoryTreeItem) newVal).getFile();
                if (dir.isDirectory()) {
                    navigateTo(dir);
                }
            }
        });
    }

    /**
     * Handles TableView double-click — opens files or navigates into folders.
     */
    private void setupTableViewEvents() {
        tableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY) {
                FileTableRow selected = tableView.getSelectionModel().getSelectedItem();
                if (selected == null) return;

                File file = selected.getFile();
                if (file.isDirectory()) {
                    // Navigate into the folder
                    navigateTo(file);
                } else {
                    // Open the file using existing backend FileOpener
                    FileOpener.open(file);
                }
            }
        });

        // Right-click context menu
        tableView.setContextMenu(createContextMenu());
    }

    /**
     * Handles address bar — pressing Enter navigates to the typed path.
     */
    private void setupAddressBarEvents() {
        addressBar.setOnAction(event -> {
            String path = addressBar.getText().trim();
            if (!path.isEmpty()) {
                File dir = new File(path);
                if (dir.exists() && dir.isDirectory()) {
                    navigateTo(dir);
                } else if (dir.exists() && dir.isFile()) {
                    // If user typed a file path, open the file
                    FileOpener.open(dir);
                } else {
                    // Invalid path — restore current
                    if (currentDirectory != null) {
                        addressBar.setText(currentDirectory.getAbsolutePath());
                    }
                }
            }
        });
    }

    /**
     * Handles search field — performs search on Enter key.
     */
    private void setupSearchEvents() {
        if (searchField == null) return;

        searchField.setOnAction(event -> {
            String query = searchField.getText().trim();
            if (query.isEmpty()) {
                // Clear search, reload current directory
                if (currentDirectory != null) {
                    loadFilesIntoTable(currentDirectory);
                    updateStatusBar();
                }
                return;
            }

            // Run search via adapter
            performSearch(query);
        });
    }

    /**
     * Updates the status bar when table selection changes.
     */
    private void setupTableSelection() {
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                String info = newVal.getName();
                if (!newVal.isDirectory()) {
                    info += "  •  " + newVal.getSizeFormatted();
                }
                statusSelection.setText(info);
            } else {
                statusSelection.setText("");
            }
        });
    }

    // ==========================================
    //  SEARCH
    // ==========================================

    /**
     * Performs a search using the backend adapter and displays results in the table.
     */
    private void performSearch(String query) {
        // Run on background thread to keep UI responsive
        javafx.concurrent.Task<List<FileInfo>> task = new javafx.concurrent.Task<>() {
            @Override
            protected List<FileInfo> call() {
                return searchAdapter.search(query);
            }
        };

        task.setOnSucceeded(event -> {
            List<FileInfo> results = task.getValue();
            ObservableList<FileTableRow> items = FXCollections.observableArrayList();

            for (FileInfo fi : results) {
                File file = new File(fi.getPathName());
                if (file.exists()) {
                    items.add(new FileTableRow(file));
                }
            }

            tableView.setItems(items);
            statusItemCount.setText(items.size() + " search results for \"" + query + "\"");
            statusSelection.setText("");
        });

        task.setOnFailed(event -> {
            statusItemCount.setText("Search failed");
        });

        new Thread(task).start();
    }

    // ==========================================
    //  CONTEXT MENU
    // ==========================================

    /**
     * Creates a right-click context menu with UI-only action stubs.
     */
    private ContextMenu createContextMenu() {
        ContextMenu menu = new ContextMenu();

        MenuItem openItem = new MenuItem("Open");
        openItem.setOnAction(e -> {
            FileTableRow selected = tableView.getSelectionModel().getSelectedItem();
            if (selected == null) return;
            File file = selected.getFile();
            if (file.isDirectory()) {
                navigateTo(file);
            } else {
                FileOpener.open(file);
            }
        });

        MenuItem renameItem = new MenuItem("Rename");
        renameItem.setOnAction(e -> {
            FileTableRow selected = tableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                System.out.println("[UI Stub] Rename: " + selected.getFile().getAbsolutePath());
            }
        });

        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(e -> {
            FileTableRow selected = tableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                System.out.println("[UI Stub] Delete: " + selected.getFile().getAbsolutePath());
            }
        });

        SeparatorMenuItem separator = new SeparatorMenuItem();

        MenuItem propertiesItem = new MenuItem("Properties");
        propertiesItem.setOnAction(e -> {
            FileTableRow selected = tableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                showPropertiesDialog(selected);
            }
        });

        menu.getItems().addAll(openItem, separator, renameItem, deleteItem,
                               new SeparatorMenuItem(), propertiesItem);
        return menu;
    }

    /**
     * Shows a simple properties dialog for a file (UI only).
     */
    private void showPropertiesDialog(FileTableRow row) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Properties");
        alert.setHeaderText(row.getName());
        alert.setContentText(
            "Type: " + row.getType() + "\n" +
            "Size: " + row.getSizeFormatted() + "\n" +
            "Modified: " + row.getLastModifiedFormatted() + "\n" +
            "Path: " + row.getFile().getAbsolutePath()
        );
        alert.showAndWait();
    }

    // ==========================================
    //  UI HELPERS
    // ==========================================

    /**
     * Updates back/forward button disabled state based on history stacks.
     */
    private void updateNavButtons() {
        backBtn.setDisable(backStack.isEmpty());
        forwardBtn.setDisable(forwardStack.isEmpty());
    }

    /**
     * Updates the status bar item count.
     */
    private void updateStatusBar() {
        int count = tableView.getItems().size();
        long folders = tableView.getItems().stream().filter(FileTableRow::isDirectory).count();
        long files = count - folders;

        StringBuilder sb = new StringBuilder();
        if (folders > 0) sb.append(folders).append(folders == 1 ? " folder" : " folders");
        if (folders > 0 && files > 0) sb.append(", ");
        if (files > 0) sb.append(files).append(files == 1 ? " file" : " files");
        if (count == 0) sb.append("Empty folder");

        statusItemCount.setText(sb.toString());
        statusSelection.setText("");
    }

    /**
     * Best-effort selection of a tree node matching the navigated directory.
     */
    private void selectTreeNode(File directory) {
        // We don't force-expand the tree to find the node (that would be slow).
        // Just update the address bar — the tree selection is supplementary.
    }

    /**
     * Returns the current directory.
     */
    public File getCurrentDirectory() {
        return currentDirectory;
    }
}
