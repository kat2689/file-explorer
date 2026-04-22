
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class FileScanner{
    public void scan(Path path) {

    
         try{
            Files.walkFileTree(path, new SimpleFileVisitor<Path>(){
           
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    System.out.println("File: " + file);
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