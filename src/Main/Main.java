package Main;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import myscanner.FileScanner;
import FileIndexing.FileIndexing;
import FileInfo.FileInfo;
import UserSearch.UserSearch;
import CacheManager.CacheManager;

public class Main {

    
public static List<FileInfo> search(String userQuery) {

    CacheManager cacheManager = new CacheManager();

    FileIndexing fileIndexing = loadIndex(cacheManager);

    List<FileInfo> fileInfo = searchFromCache(fileIndexing, userQuery);

    if (fileInfo == null || fileInfo.isEmpty()) {
        Set<String> visitFile=visitedFile(fileIndexing);
        fileInfo = handleNoResult(fileIndexing, cacheManager, userQuery,visitFile);
    }
   

    return fileInfo;
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
     
    public static  Set<String> visitedFile(FileIndexing fileIndexing){
           return fileIndexing.getVisitedPaths();

        }




        public static List<FileInfo> handleNoResult(
            FileIndexing fileIndexing,
            CacheManager cacheManager,
            String query,
            Set<String> visitedFile) {
    
        Path basePath = Path.of("C:/MY FOLDER/Rishi Folder");
    
        FileScanner fileScanner = new FileScanner(fileIndexing);
        UserSearch userSearch = new UserSearch(fileIndexing);
    
        // exact file search like hello.java
        if (query.contains(".")) {
    
            executeScanAndCache(fileScanner, fileIndexing, cacheManager, basePath, query, visitedFile);
    
            return new UserSearch(fileIndexing).searchFile(query);
        }
    
        if (!fileIndexing.isEmpty()) {
    
            List<FileInfo> fileInformation = userSearch.partialSearch(query);
    
            if (!fileInformation.isEmpty()) {
                return fileInformation;
            }
    
            executeScanAndCache(fileScanner, fileIndexing, cacheManager, basePath, query, visitedFile);
    
            return new UserSearch(fileIndexing).partialSearch(query);
        }
    
        executeScanAndCache(fileScanner, fileIndexing, cacheManager, basePath, query, visitedFile);
    
        return new UserSearch(fileIndexing).partialSearch(query);
    }
    private static void executeScanAndCache(
        FileScanner fileScanner,
        FileIndexing fileIndexing,
        CacheManager cacheManager,
        Path basePath,
        String query,
        Set<String> visitedFile) {

    fileScanner.scanQuery(basePath, query, visitedFile);
    cacheManager.createCacheFile(fileIndexing);
}



            public static void printResult(List<FileInfo> fileInfo) {

                if (fileInfo == null || fileInfo.isEmpty()) {
                    System.out.println("No file found");
                } else {
                    for (FileInfo fi : fileInfo) {
                        System.out.println(fi.getPathName());
                    }
                   
                }
            }





}



