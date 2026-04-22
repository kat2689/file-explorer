import java.nio.file.Path;


public class Main {

    public static void main(String[] args) {
        // using path class from java nio file
        Path startPath = Path.of("C:/MY FOLDER/Rishi Folder/Personal Projects");

        FileScanner fileScanner = new FileScanner();
        fileScanner.scan(startPath);

    }
}