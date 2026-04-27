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
        
        System.out.println("PARTIAL");
        System.out.println(index.partialFileInfo(file));

        return index.partialFileInfo(file);

    }


    public  List<FileInfo>  searchFile(String file){
    
        if(file.startsWith(".")){
            System.out.println("EXTENSION");
            System.out.println( index.getExtFileInfo(file));
            return index.getExtFileInfo(file);
        }
         else {
            System.out.println("EXACT");
            System.out.println( index.getNameFileInfo(file));
            return index.getNameFileInfo(file);


    
         }
        




        
    }


    
}
