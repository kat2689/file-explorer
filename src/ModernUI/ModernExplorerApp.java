package ModernUI;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.File;
import java.util.Comparator;

/**
 * ModernExplorerApp — Main layout builder for the File Explorer UI.
 *
 * Assembles the full BorderPane layout:
 *   - TOP: Navigation toolbar (Back, Forward, Up, Refresh, Address Bar, Search)
 *   - CENTER: SplitPane with TreeView (left) and TableView (right)
 *   - BOTTOM: Status bar (item count, selection info)
 *
 * This class is purely UI construction — all event logic is in ModernExplorerController.
 */
public class ModernExplorerApp {

    private final ModernExplorerController controller = new ModernExplorerController();
    private final File startDirectory;

    // UI Components (constructed during build)
    private TreeView<String> treeView;
    private TableView<FileTableRow> tableView;
    private TextField addressBar;
    private TextField searchField;
    private Label statusItemCount;
    private Label statusSelection;
    private Button backBtn;
    private Button forwardBtn;
    private Button upBtn;
    private Button refreshBtn;

    /**
     * Creates the app with a specific starting directory.
     */
    public ModernExplorerApp(File startDirectory) {
        this.startDirectory = startDirectory;
    }

    /**
     * Builds and configures the full scene, then shows it on the given stage.
     */
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("root");

        // Build all sections
        root.setTop(buildNavigationBar());
        root.setCenter(buildCenterContent());
        root.setBottom(buildStatusBar());

        // Create scene
        Scene scene = new Scene(root, 1100, 700);

        // Load CSS stylesheet
        String cssPath = getClass().getResource("style.css") != null
            ? getClass().getResource("style.css").toExternalForm()
            : null;
        if (cssPath != null) {
            scene.getStylesheets().add(cssPath);
        }

        // Initialize the controller with all component references
        controller.init(treeView, tableView, addressBar, searchField,
                        statusItemCount, statusSelection, backBtn, forwardBtn);

        // Navigate to start directory
        controller.navigateTo(startDirectory);

        // Configure stage
        stage.setTitle("File Explorer");
        stage.setScene(scene);
        stage.setMinWidth(600);
        stage.setMinHeight(400);
        stage.show();
    }

    // ==========================================
    //  NAVIGATION BAR (Top)
    // ==========================================

    /**
     * Builds the top navigation toolbar with Back, Forward, Up, Refresh,
     * Address Bar, and Search Field.
     */
    private HBox buildNavigationBar() {
        HBox toolbar = new HBox();
        toolbar.getStyleClass().add("nav-toolbar");
        toolbar.setAlignment(Pos.CENTER_LEFT);
        toolbar.setSpacing(4);

        // --- Navigation Buttons ---
        backBtn = createNavButton(IconHelper.backArrow(16), "Back (Alt+Left)");
        backBtn.setOnAction(e -> controller.goBack());
        backBtn.setDisable(true);

        forwardBtn = createNavButton(IconHelper.forwardArrow(16), "Forward (Alt+Right)");
        forwardBtn.setOnAction(e -> controller.goForward());
        forwardBtn.setDisable(true);

        upBtn = createNavButton(IconHelper.upArrow(16), "Up one level");
        upBtn.setOnAction(e -> controller.goUp());

        refreshBtn = createNavButton(IconHelper.refreshIcon(16), "Refresh");
        refreshBtn.setOnAction(e -> controller.refresh());

        // Separator
        Region sep1 = new Region();
        sep1.setPrefWidth(8);

        // --- Address Bar ---
        addressBar = new TextField();
        addressBar.getStyleClass().add("address-bar");
        addressBar.setPromptText("Enter path...");
        HBox.setHgrow(addressBar, Priority.ALWAYS);

        // Separator
        Region sep2 = new Region();
        sep2.setPrefWidth(8);

        // --- Search Field ---
        searchField = new TextField();
        searchField.getStyleClass().add("search-field");
        searchField.setPromptText("\uD83D\uDD0D Search files...");
        searchField.setPrefWidth(220);

        toolbar.getChildren().addAll(
            backBtn, forwardBtn, upBtn, refreshBtn,
            sep1, addressBar, sep2, searchField
        );

        return toolbar;
    }

    /**
     * Creates a styled navigation button with an icon and tooltip.
     */
    private Button createNavButton(Node icon, String tooltipText) {
        Button btn = new Button();
        btn.setGraphic(icon);
        btn.getStyleClass().add("nav-button");
        btn.setTooltip(new Tooltip(tooltipText));
        btn.setFocusTraversable(false);
        return btn;
    }

    // ==========================================
    //  CENTER CONTENT (SplitPane)
    // ==========================================

    /**
     * Builds the center SplitPane with TreeView (left) and TableView (right).
     */
    private SplitPane buildCenterContent() {
        SplitPane splitPane = new SplitPane();
        splitPane.setOrientation(Orientation.HORIZONTAL);
        splitPane.setDividerPositions(0.22);

        // Left panel — Directory Tree
        VBox treePanel = buildTreePanel();
        SplitPane.setResizableWithParent(treePanel, false);

        // Right panel — File Table
        VBox tablePanel = buildTablePanel();

        splitPane.getItems().addAll(treePanel, tablePanel);
        return splitPane;
    }

    /**
     * Builds the left panel containing the directory TreeView.
     */
    private VBox buildTreePanel() {
        VBox panel = new VBox();
        panel.getStyleClass().add("tree-panel");
        panel.setMinWidth(180);

        treeView = new TreeView<>();
        treeView.getStyleClass().add("tree-view");
        treeView.setShowRoot(false);
        VBox.setVgrow(treeView, Priority.ALWAYS);

        // Build root with system drives
        TreeItem<String> invisibleRoot = new TreeItem<>("Root");
        invisibleRoot.setExpanded(true);

        File[] roots = File.listRoots();
        if (roots != null) {
            for (File root : roots) {
                DirectoryTreeItem driveItem = new DirectoryTreeItem(root);
                driveItem.setExpanded(false);
                invisibleRoot.getChildren().add(driveItem);
            }
        }

        // Also add quick-access to common folders
        File userHome = new File(System.getProperty("user.home"));
        if (userHome.exists()) {
            String[] quickAccess = {"Desktop", "Documents", "Downloads", "Pictures", "Videos"};
            TreeItem<String> quickAccessRoot = new TreeItem<>("Quick Access");
            quickAccessRoot.setExpanded(true);

            for (String folderName : quickAccess) {
                File folder = new File(userHome, folderName);
                if (folder.exists() && folder.isDirectory()) {
                    DirectoryTreeItem item = new DirectoryTreeItem(folder);
                    quickAccessRoot.getChildren().add(item);
                }
            }

            if (!quickAccessRoot.getChildren().isEmpty()) {
                invisibleRoot.getChildren().add(0, quickAccessRoot);
            }
        }

        treeView.setRoot(invisibleRoot);

        panel.getChildren().add(treeView);
        return panel;
    }

    /**
     * Builds the right panel containing the file TableView.
     */
    @SuppressWarnings("unchecked")
    private VBox buildTablePanel() {
        VBox panel = new VBox();

        tableView = new TableView<>();
        tableView.getStyleClass().add("table-view");
        tableView.setPlaceholder(new Label("This folder is empty"));
        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        VBox.setVgrow(tableView, Priority.ALWAYS);

        // --- Column: Icon + Name ---
        TableColumn<FileTableRow, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(320);
        nameCol.setMinWidth(150);

        // Custom cell to show icon + name
        nameCol.setCellFactory(column -> new TableCell<FileTableRow, String>() {
            @Override
            protected void updateItem(String name, boolean empty) {
                super.updateItem(name, empty);
                if (empty || name == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    FileTableRow row = getTableView().getItems().get(getIndex());
                    Node icon = row.isDirectory()
                        ? IconHelper.folderIcon(16)
                        : IconHelper.fileIcon(16);

                    setText(name);
                    setGraphic(icon);
                    setGraphicTextGap(8);
                }
            }
        });

        // Sort by name — directories first
        nameCol.setComparator((a, b) -> a.compareToIgnoreCase(b));

        // --- Column: Type ---
        TableColumn<FileTableRow, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        typeCol.setPrefWidth(100);
        typeCol.setMinWidth(60);

        // --- Column: Size ---
        TableColumn<FileTableRow, String> sizeCol = new TableColumn<>("Size");
        sizeCol.setCellValueFactory(new PropertyValueFactory<>("sizeFormatted"));
        sizeCol.setPrefWidth(100);
        sizeCol.setMinWidth(60);
        sizeCol.setStyle("-fx-alignment: CENTER-RIGHT;");

        // Sort by actual byte size, not formatted string
        sizeCol.setComparator((a, b) -> {
            long sizeA = parseSizeHint(a);
            long sizeB = parseSizeHint(b);
            return Long.compare(sizeA, sizeB);
        });

        // --- Column: Date Modified ---
        TableColumn<FileTableRow, String> dateCol = new TableColumn<>("Date Modified");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("lastModifiedFormatted"));
        dateCol.setPrefWidth(180);
        dateCol.setMinWidth(100);

        tableView.getColumns().addAll(nameCol, typeCol, sizeCol, dateCol);

        // Make columns fill width
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        panel.getChildren().add(tableView);
        return panel;
    }

    /**
     * Helper to extract a sortable size value from the formatted string.
     */
    private long parseSizeHint(String formatted) {
        if (formatted == null || formatted.isEmpty()) return -1;
        try {
            String num = formatted.replaceAll("[^0-9.]", "");
            double val = Double.parseDouble(num);
            if (formatted.contains("GB")) return (long)(val * 1024 * 1024 * 1024);
            if (formatted.contains("MB")) return (long)(val * 1024 * 1024);
            if (formatted.contains("KB")) return (long)(val * 1024);
            return (long) val;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    // ==========================================
    //  STATUS BAR (Bottom)
    // ==========================================

    /**
     * Builds the bottom status bar showing item counts and selection info.
     */
    private HBox buildStatusBar() {
        HBox statusBar = new HBox();
        statusBar.getStyleClass().add("status-bar");
        statusBar.setAlignment(Pos.CENTER_LEFT);
        statusBar.setSpacing(20);

        statusItemCount = new Label("0 items");
        statusItemCount.getStyleClass().add("status-label");

        // Vertical separator
        Region separator = new Region();
        separator.setPrefWidth(1);
        separator.setMinHeight(14);
        separator.setStyle("-fx-background-color: #ced4da;");

        statusSelection = new Label("");
        statusSelection.getStyleClass().add("status-label-bold");

        // Spacer to push right-side info
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        statusBar.getChildren().addAll(statusItemCount, separator, statusSelection, spacer);
        return statusBar;
    }
}
