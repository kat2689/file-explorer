package CacheManager;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import FileIndexing.FileIndexing;

public class CacheManager {
       
       
        public void createCacheFile(FileIndexing File) {
            try (ObjectOutputStream out =
                     new ObjectOutputStream(new FileOutputStream("index.dat"))) {
        
                out.writeObject(File);
                System.out.println("Cache saved successfully");
        
            } catch (IOException e) {
                System.err.println("Failed to save cache");
                e.printStackTrace();
            }
        }


   public FileIndexing loadCache() {
    try (ObjectInputStream in =
             new ObjectInputStream(new FileInputStream("index.dat"))) {

    FileIndexing index = (FileIndexing) in.readObject();
    return index;
       

        

    } catch (IOException | ClassNotFoundException e) {
        e.printStackTrace();
    }
    return null;
}
    
}
