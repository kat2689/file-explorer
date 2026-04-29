
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
        String userWantedFile = "xtyc.com";

        CacheManager cacheManager = new CacheManager();

        FileIndexing fileIndexing = loadIndex(cacheManager);

        List<FileInfo> fileInfo = searchFromCache(fileIndexing, userWantedFile);

        if (fileInfo == null || fileInfo.isEmpty()) {
            fileInfo = handleNoResult(fileIndexing, cacheManager, userWantedFile);
        }

        printResult(fileInfo);

        

       
   
       

    
}





    public static FileIndexing loadIndex(CacheManager cacheManager) {

        File file = new File("index.dat");

        if (file.exists()) {

            System.out.println("Cache exists");

            FileIndexing loaded = cacheManager.loadCache();

            if (loaded != null) {
                return loaded;
            }
        }

        System.out.println("Creating new cache...");

        FileIndexing fileIndexing = new FileIndexing();

        FileScanner fileScanner = new FileScanner(fileIndexing);

        fileScanner.scan(
                Path.of("C:/MY FOLDER/Rishi Folder/Personal Projects")
        );
        cacheManager.createCacheFile(fileIndexing);
    

        return fileIndexing;
    }




    
    public static List<FileInfo> searchFromCache(FileIndexing fileIndexing, String query) {

        if (fileIndexing == null || fileIndexing.isEmpty()) {
            return null;
        }

        UserSearch userSearch = new UserSearch(fileIndexing);

        return userSearch.searchFile(query);
    }




    public static List<FileInfo> handleNoResult(FileIndexing fileIndexing,
        CacheManager cacheManager,
        String query) {

             
            
            
                // exact file search like hello.java
                if (query.contains(".")) {

                FileScanner fileScanner = new FileScanner(fileIndexing);

                fileScanner.scanQuery(
                Path.of("C:/MY FOLDER/Rishi Folder"),
                query
                );

                cacheManager.createCacheFile(fileIndexing);

                UserSearch userSearch = new UserSearch(fileIndexing);

                return userSearch.searchFile(query);
                }
                else {

                    if (!fileIndexing.isEmpty()) {
        
                        UserSearch userSearch = new UserSearch(fileIndexing);
        
                        
                        if( userSearch.partialSearch(query).isEmpty()){
                            return diskScanner(fileIndexing, cacheManager, query);

                        } 
                        return userSearch.partialSearch(query);
                    }else{

                            
                            
                            return diskScanner(fileIndexing, cacheManager, query);
                        }

                    }
                
                
        
               
            }
            public static List<FileInfo> diskScanner(FileIndexing fileIndexing,
                CacheManager cacheManager,
                String query){
                    FileScanner fileScanner = new FileScanner(fileIndexing);

                    fileScanner.scan(
                        Path.of("C:/MY FOLDER/Rishi Folder")
                    );

                    cacheManager.createCacheFile(fileIndexing);
                    UserSearch userSearch = new UserSearch(fileIndexing);

                    return userSearch.partialSearch(query);

                
            }




            public static void printResult(List<FileInfo> fileInfo) {

                if (fileInfo == null || fileInfo.isEmpty()) {
                    System.out.println("No file found");
                } else {
                    System.out.println(fileInfo.get(0).getFileName());
                }
            }





}



