package myscanner;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import FileInfo.FileInfo;

public class FileScanner{
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
                
                    // For now: print it (later send to index)
                    System.out.println("this is working");
                    System.out.println("this is working"+info);
                  
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