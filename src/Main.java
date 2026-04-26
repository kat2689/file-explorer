import java.io.File;
import java.nio.file.Path;
import java.util.Scanner;

import myscanner.FileScanner;
import FileIndexing.FileIndexing;
import UserSearch.UserSearch;
import CacheManager.CacheManager;

public class Main {

    public static void main(String[] args) {
        File file = new File("index.dat");
        
        FileIndexing fileIndexing=new FileIndexing();         
        FileScanner fileScanner = new FileScanner(fileIndexing);
        CacheManager cacheManager= new CacheManager(fileIndexing);


        

        if (file.exists()) {
    System.out.println("Cache exists");

    } else{
        Path startPath = Path.of("C:/MY FOLDER/Rishi Folder/Personal Projects");

        fileScanner.scan(startPath);
         try{cacheManager.createCacheFile();}
         catch(Exception e){
            System.out.println("error"+e);

         }
        



    }
        // using path class from java nio file
       

        // experiment 
        
        // Scanner fileSearch = new Scanner(System.in);
        // System.out.println("Enter filename");
    
        // String fileName= fileSearch.nextLine(); 
     
        // UserSearch userSearch=new UserSearch(fileIndexing);
        // userSearch.searchFile(fileName);
       
        // System.out.println(UserSearch.instantSearch(fileName));



    }

}
