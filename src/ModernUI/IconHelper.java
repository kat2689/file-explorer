package ModernUI;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

/**
 * IconHelper — Provides programmatic SVG-style icons for the File Explorer UI.
 * All icons are built with JavaFX shapes, so no external image files are needed.
 * This makes the UI fully self-contained and drop-in ready.
 */
public class IconHelper {

    // --- Color Constants ---
    private static final Color FOLDER_YELLOW = Color.web("#FFC107");
    private static final Color FOLDER_DARK   = Color.web("#FFA000");
    private static final Color FILE_BLUE     = Color.web("#90CAF9");
    private static final Color FILE_DARK     = Color.web("#42A5F5");
    private static final Color DRIVE_GRAY    = Color.web("#78909C");
    private static final Color DRIVE_LIGHT   = Color.web("#B0BEC5");
    private static final Color ARROW_COLOR   = Color.web("#455A64");
    private static final Color REFRESH_COLOR = Color.web("#0078D4");

    /**
     * Creates a 16x16 folder icon.
     */
    public static Node folderIcon(double size) {
        Group group = new Group();

        // Folder tab (top-left flap)
        Polygon tab = new Polygon(
            0, size * 0.25,
            size * 0.4, size * 0.25,
            size * 0.5, size * 0.15,
            0, size * 0.15
        );
        tab.setFill(FOLDER_DARK);

        // Folder body
        Rectangle body = new Rectangle(0, size * 0.25, size, size * 0.65);
        body.setArcWidth(size * 0.1);
        body.setArcHeight(size * 0.1);
        body.setFill(FOLDER_YELLOW);

        group.getChildren().addAll(tab, body);
        return group;
    }

    /**
     * Creates a 16x16 file/document icon.
     */
    public static Node fileIcon(double size) {
        Group group = new Group();

        // Page body
        double foldSize = size * 0.3;
        Polygon page = new Polygon(
            0, 0,
            size - foldSize, 0,
            size, foldSize,
            size, size,
            0, size
        );
        page.setFill(FILE_BLUE);
        page.setStroke(FILE_DARK);
        page.setStrokeWidth(0.5);

        // Corner fold
        Polygon fold = new Polygon(
            size - foldSize, 0,
            size - foldSize, foldSize,
            size, foldSize
        );
        fold.setFill(FILE_DARK);
        fold.setOpacity(0.5);

        // Text lines
        for (int i = 0; i < 3; i++) {
            double y = size * 0.35 + i * size * 0.15;
            Line line = new Line(size * 0.15, y, size * 0.75, y);
            line.setStroke(Color.web("#1565C0"));
            line.setStrokeWidth(0.8);
            group.getChildren().add(line);
        }

        group.getChildren().addAll(0, java.util.List.of(page, fold));
        return group;
    }

    /**
     * Creates a 16x16 drive icon.
     */
    public static Node driveIcon(double size) {
        Group group = new Group();

        // Drive body (3D box effect)
        Polygon top = new Polygon(
            size * 0.1, size * 0.3,
            size * 0.9, size * 0.3,
            size * 0.8, size * 0.15,
            size * 0.2, size * 0.15
        );
        top.setFill(DRIVE_LIGHT);

        Rectangle body = new Rectangle(size * 0.1, size * 0.3, size * 0.8, size * 0.5);
        body.setArcWidth(size * 0.08);
        body.setArcHeight(size * 0.08);
        body.setFill(DRIVE_GRAY);

        // LED indicator
        Circle led = new Circle(size * 0.75, size * 0.65, size * 0.05);
        led.setFill(Color.web("#4CAF50"));

        group.getChildren().addAll(top, body, led);
        return group;
    }

    /**
     * Creates a back arrow icon.
     */
    public static Node backArrow(double size) {
        Polygon arrow = new Polygon(
            size * 0.6, size * 0.15,
            size * 0.2, size * 0.5,
            size * 0.6, size * 0.85
        );
        arrow.setFill(ARROW_COLOR);
        arrow.setStroke(Color.TRANSPARENT);
        return arrow;
    }

    /**
     * Creates a forward arrow icon.
     */
    public static Node forwardArrow(double size) {
        Polygon arrow = new Polygon(
            size * 0.4, size * 0.15,
            size * 0.8, size * 0.5,
            size * 0.4, size * 0.85
        );
        arrow.setFill(ARROW_COLOR);
        arrow.setStroke(Color.TRANSPARENT);
        return arrow;
    }

    /**
     * Creates an up arrow icon.
     */
    public static Node upArrow(double size) {
        Polygon arrow = new Polygon(
            size * 0.5, size * 0.15,
            size * 0.2, size * 0.65,
            size * 0.4, size * 0.65,
            size * 0.4, size * 0.85,
            size * 0.6, size * 0.85,
            size * 0.6, size * 0.65,
            size * 0.8, size * 0.65
        );
        arrow.setFill(ARROW_COLOR);
        return arrow;
    }

    /**
     * Creates a refresh/reload icon (circular arrow).
     */
    public static Node refreshIcon(double size) {
        Group group = new Group();

        Arc arc = new Arc(size * 0.5, size * 0.5, size * 0.3, size * 0.3, 30, 300);
        arc.setType(ArcType.OPEN);
        arc.setFill(Color.TRANSPARENT);
        arc.setStroke(REFRESH_COLOR);
        arc.setStrokeWidth(size * 0.12);
        arc.setStrokeLineCap(StrokeLineCap.ROUND);

        // Arrowhead at the end of the arc
        Polygon arrowHead = new Polygon(
            size * 0.72, size * 0.18,
            size * 0.85, size * 0.38,
            size * 0.6, size * 0.35
        );
        arrowHead.setFill(REFRESH_COLOR);

        group.getChildren().addAll(arc, arrowHead);
        return group;
    }

    /**
     * Creates a search/magnifying glass icon.
     */
    public static Node searchIcon(double size) {
        Group group = new Group();

        Circle lens = new Circle(size * 0.38, size * 0.38, size * 0.25);
        lens.setFill(Color.TRANSPARENT);
        lens.setStroke(ARROW_COLOR);
        lens.setStrokeWidth(size * 0.1);

        Line handle = new Line(size * 0.58, size * 0.58, size * 0.85, size * 0.85);
        handle.setStroke(ARROW_COLOR);
        handle.setStrokeWidth(size * 0.12);
        handle.setStrokeLineCap(StrokeLineCap.ROUND);

        group.getChildren().addAll(lens, handle);
        return group;
    }
}
