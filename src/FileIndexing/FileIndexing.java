package FileIndexing;
import FileInfo.FileInfo;

import java.util.List;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FileIndexing implements Serializable {
    // this map store key(filename) and value(fileinfo class instance) 
    // for eg->hello.class:{{fileinfo instance1},{fileinfo instance2}} because a file can have duplicate name

    private  Map<String, List<FileInfo>> fileMap = new HashMap<>();
    private  Map<String, List<FileInfo>> extMap  = new HashMap<>();
    // this fuction is to push the key and value to map
    public void setFileMap( FileInfo fi) {
        String str=fi.getFileName().toLowerCase();
        System.out.println("ADDING FILE: " + fi.getPathName());
        fileMap.computeIfAbsent(str, k -> new java.util.ArrayList<>()).add(fi);
    }
    public void setExtMap(FileInfo fi) {
        String fileExt = fi.getExtName().toLowerCase();
        if (fileExt != null) {
            fileExt = fileExt.toLowerCase();
        

        }
      

     if (fileExt == null || fileExt.isEmpty()) {
        return;
    }
        
    
      extMap.computeIfAbsent(fileExt, k -> new ArrayList<>()).add(fi);
    }
    
  
    public List<FileInfo> getExtFileInfo(String str){
        return extMap.get(str);

    }
    public List<FileInfo> getNameFileInfo(String str){
        
        return fileMap.get(str);

    }
    public List<FileInfo> partialFileInfo(String str) {
        String search = str.toLowerCase();
       
    
        return fileMap.entrySet().stream()
                .filter(entry -> entry.getKey().toLowerCase().contains(search))
                .flatMap(entry -> entry.getValue().stream())
                .toList();  
    }
    public void printFileMap() {
            fileMap.forEach((key, fileList) -> {
                System.out.println("Key: " + key);
                fileList.forEach(file -> {
                    System.out.println("  File: " + file.getPathName());
                });
            });
        }

        public boolean isEmpty() {
            return fileMap.isEmpty() && extMap.isEmpty();
        }

    
    
}