package FileInfo;

public class FileInfo {
    private String fileName;
    private String fullPath;
    private String extension;
    private boolean isDirectory;
    private long lastModified;
    private long fileSize;

    public FileInfo(String fileName, String fullPath, String extension,
                    boolean isDirectory, long lastModified, long fileSize) {
        this.fileName = fileName;
        this.fullPath = fullPath;
        this.extension = extension;
        this.isDirectory = isDirectory;
        this.lastModified = lastModified;
        this.fileSize = fileSize;
    }

}