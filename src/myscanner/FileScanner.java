package myscanner;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;


import FileInfo.FileInfo;
import FileIndexing.FileIndexing;

public class FileScanner{
    
    private FileIndexing index;
    // so what happening here is, we set the path in main.java and created the instance of fileindexing and then we created the instance of filescanner instance which takes the instance of fileindexing , so till now  we have instance of fileindexing ,and instance of filescanner which have the  attribute of fileindexing , instance of filescanner name is filescanner, now we use the method scan of filescanner class and pass the Path we already set , pass as arguement and inside scan method there is method walkfiletree, walkfiletree this method traverse the path that pass as arguement in depth ( about walkfiletree method ->have two arguement path, anonymous class instance that implement simplefilevisitor )
    public  FileScanner(FileIndexing index){
        this.index=index;

    }
    public void scan(Path path) {
       
    
         try{
            Files.walkFileTree(path, new SimpleFileVisitor<Path>(){
           
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    String fullPath = file.toString();

                    // 2. file name
                    String fileName = file.getFileName().toString();
                
                    // 3. extension
                    String extension = "";
                    int dotIndex = fileName.lastIndexOf(".");
                    if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
                        extension = fileName.substring(dotIndex); // includes "."
                    }
                
                    // 4. file size
                    long fileSize = attrs.size();
                
                    // 5. last modified time
                    long lastModified = attrs.lastModifiedTime().toMillis();
                
                    // 6. is directory (always false here, but keeping clean design)
                    boolean isDirectory = false;
                
                    // 7. create FileInfo object
                    FileInfo info = new FileInfo(
                            fileName,
                            fullPath,
                            extension,
                            isDirectory,
                            lastModified,
                            fileSize
                    );
                    index.setFileMap(info);
                    index.setExtMap(info);
                    // index.printFileMap();



                    


               index.printFileMap();
                    // For now: print it (later send to index)
                    // System.out.println("this is working");
                    // System.out.println("this is working"+info);
                  
                    return FileVisitResult.CONTINUE;
                }
                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    System.err.println("Error accessing file: " + file);
                    return FileVisitResult.CONTINUE;
                }



            });
         } catch (IOException e) {
            System.err.println("Error traversing directory: " + e.getMessage());
        }

        
        
        // why not Files.walk() because if there is accessdenied file then stream will be closed and there will not tranversal of file and folder------------------------------------
        // using try with resource to close the stream automatically
        // try(Stream<Path> stream = Files.walk(path)) {
        //     System.out.println("hello");
        //     stream.forEach(p->{
        //         try{
        //             if(Files.isRegularFile(p)){
        //                 System.out.println("file"+p);
        //             }
        //         }catch(Exception e){
        //             System.out.println("Skipping: " + p);
        //         }
        //     });



            
            
        // }catch( Exception e){
        //     System.out.println("outer catch block: " + e.getMessage());

        // }


        

    }
    
}