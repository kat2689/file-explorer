package CacheManager;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import FileIndexing.FileIndexing;

public class CacheManager {
        private FileIndexing index;
        public  CacheManager(FileIndexing index){
            this.index=index;
    
        }
    public void createCacheFile() throws IOException{
        FileOutputStream file = new FileOutputStream("index.dat");
         ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(index);
            out.close();
            file.close();

        
    }
    
}
