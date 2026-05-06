package ModernUI;

import java.util.List;
import java.util.ArrayList;

import FileInfo.FileInfo;
import Main.Main;

/**
 * SearchAdapter — Wraps the existing backend search API (Main.search())
 * for use from the new UI layer. This adapter ensures we never modify
 * backend code while still integrating search functionality.
 *
 * Runs search synchronously (can be called from a background thread
 * by the controller to keep UI responsive).
 */
public class SearchAdapter {

    /**
     * Performs a search using the existing backend search engine.
     *
     * @param query The search query string
     * @return List of FileInfo results, never null
     */
    public List<FileInfo> search(String query) {
        if (query == null || query.trim().isEmpty()) {
            return new ArrayList<>();
        }

        try {
            List<FileInfo> results = Main.search(query.trim());
            return results != null ? results : new ArrayList<>();
        } catch (Exception e) {
            System.err.println("Search error: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
