package UserSearch;
import FileIndexing.FileIndexing;
import FileInfo.FileInfo;

import java.util.List;
import java.util.Scanner; 
// experiment


public class UserSearch {
    private FileIndexing index;
    public  UserSearch(FileIndexing index){
        this.index=index;

    }


    public  List<FileInfo>  searchFile(String file){
    
        if(file.startsWith(".")){
            System.out.println("EXTENSION");
            System.out.println( index.getExtFileInfo(file));
            return index.getExtFileInfo(file);
        }
         else if (file.contains(".")){
            System.out.println("EXACT");
            System.out.println( index.getNameFileInfo(file));
            return index.getNameFileInfo(file);


    
         }
         else{ 

            System.out.println("PARTIAL");
            System.out.println(index.partialFileInfo(file));

            return index.partialFileInfo(file);
          

         }
     




        
    }


    
}
