package UserSearch;
import FileIndexing.FileIndexing;
import FileInfo.FileInfo;

import java.util.List;

// experiment


public class UserSearch {
    private FileIndexing index;
    public  UserSearch(FileIndexing index){
        this.index=index;

    }
    public List<FileInfo> partialSearch(String file){
       
        return index.partialFileInfo(file);

    }


    public  List<FileInfo>  searchFile(String file){
    
        if(file.startsWith(".")){
          
            return index.getExtFileInfo(file);
        }
         else {
          
            return index.getNameFileInfo(file);


    
         }
        




        
    }


    
}
