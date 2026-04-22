import java.nio.file.Path;


import myscanner.FileScanner;
import FileIndexing.FileIndexing;

public class Main {

    public static void main(String[] args) {
        // using path class from java nio file
        Path startPath = Path.of("C:/MY FOLDER/Rishi Folder/Personal Projects");

        FileIndexing fileIndexing=new FileIndexing();
        FileScanner fileScanner = new FileScanner(fileIndexing);
        fileScanner.scan(startPath);

    }
}