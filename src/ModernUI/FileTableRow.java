package ModernUI;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * FileTableRow — A view-model wrapper around java.io.File for use in the TableView.
 * Provides formatted display values for name, type, size, and last-modified date.
 * This is purely a UI data class — no backend logic.
 */
public class FileTableRow {

    private final File file;
    private final String name;
    private final String type;
    private final long sizeBytes;
    private final String sizeFormatted;
    private final long lastModifiedMillis;
    private final String lastModifiedFormatted;
    private final boolean isDirectory;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM dd, yyyy  hh:mm a");

    public FileTableRow(File file) {
        this.file = file;
        this.name = file.getName();
        this.isDirectory = file.isDirectory();

        // Determine type label
        if (isDirectory) {
            this.type = "Folder";
        } else {
            String ext = getExtension(file.getName());
            this.type = ext.isEmpty() ? "File" : ext.toUpperCase();
        }

        // Size (directories show blank)
        if (isDirectory) {
            this.sizeBytes = -1;
            this.sizeFormatted = "";
        } else {
            this.sizeBytes = file.length();
            this.sizeFormatted = formatSize(sizeBytes);
        }

        // Last modified
        this.lastModifiedMillis = file.lastModified();
        if (lastModifiedMillis > 0) {
            this.lastModifiedFormatted = DATE_FORMAT.format(new Date(lastModifiedMillis));
        } else {
            this.lastModifiedFormatted = "";
        }
    }

    // --- Getters ---

    public File getFile() { return file; }
    public String getName() { return name; }
    public String getType() { return type; }
    public long getSizeBytes() { return sizeBytes; }
    public String getSizeFormatted() { return sizeFormatted; }
    public long getLastModifiedMillis() { return lastModifiedMillis; }
    public String getLastModifiedFormatted() { return lastModifiedFormatted; }
    public boolean isDirectory() { return isDirectory; }

    // --- Utility Methods ---

    /**
     * Extracts the file extension (without dot) from a filename.
     */
    private static String getExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1);
        }
        return "";
    }

    /**
     * Formats a byte count into a human-readable size string.
     */
    private static String formatSize(long bytes) {
        if (bytes < 0) return "";
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return String.format("%.1f KB", bytes / 1024.0);
        if (bytes < 1024 * 1024 * 1024) return String.format("%.1f MB", bytes / (1024.0 * 1024));
        return String.format("%.2f GB", bytes / (1024.0 * 1024 * 1024));
    }
}
