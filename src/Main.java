
import java.io.File;
import java.nio.file.Path;
import java.util.List;


import myscanner.FileScanner;
import FileIndexing.FileIndexing;
import FileInfo.FileInfo;
import UserSearch.UserSearch;
import CacheManager.CacheManager;

public class Main {

    public static void main(String[] args) {
        File file = new File("index.dat");
        FileIndexing fileIndexing;
        
       
        CacheManager cacheManager= new CacheManager();
        UserSearch userSearch;
        String userWantedFile="hello.class";


        

        if (file.exists()) {
        System.out.println("Cache exists");
        List<FileInfo> fileInfo = null;
       
        fileIndexing=cacheManager.loadCache(); 
        
        if(fileIndexing!=null){
             userSearch=new UserSearch(fileIndexing) ;
       
       fileInfo= userSearch.searchFile(userWantedFile);
    }
       if(fileInfo==null|| fileInfo.isEmpty()){









       }else{
        System.out.println(fileInfo);
       }

    

    



    } else{
        Path startPath = Path.of("C:/MY FOLDER/Rishi Folder/Personal Projects");
        fileIndexing=new FileIndexing();        
        FileScanner fileScanner = new FileScanner(fileIndexing);
        fileScanner.scan(startPath);
         cacheManager.createCacheFile(fileIndexing);
         
        



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
