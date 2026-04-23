import java.nio.file.Path;
import java.util.Scanner;

import myscanner.FileScanner;
import FileIndexing.FileIndexing;
import UserSearch.UserSearch;

public class Main {

    public static void main(String[] args) {
        // using path class from java nio file
        Path startPath = Path.of("C:/MY FOLDER/Rishi Folder/Personal Projects");

        FileIndexing fileIndexing=new FileIndexing();
        FileScanner fileScanner = new FileScanner(fileIndexing);
        fileScanner.scan(startPath);
        // experiment 
        
        // Scanner fileSearch = new Scanner(System.in);
        // System.out.println("Enter username");
    
        // String fileName= fileSearch.nextLine(); 
        // System.out.println("filename is: " + fileName);  
       
        // System.out.println(UserSearch.instantSearch(fileName));



    }

}