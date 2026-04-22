package FileIndexing;
import FileInfo.FileInfo;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FileIndexing {
    // this map store key(filename) and value(fileinfo class instance) 
    // for eg->hello.class:{{fileinfo instance1},{fileinfo instance2}} because a file can have duplicate name

    private  Map<String, List<FileInfo>> fileMap = new HashMap<>();
    // this fuction is to push the key and value to map
    public void setFileMap( FileInfo fi) {
        String str=fi.getFileName().toLowerCase();
        fileMap.computeIfAbsent(str, k -> new java.util.ArrayList<>()).add(fi);
    }
    
    public List<FileInfo> search(String name) {
        return fileMap.getOrDefault(name.toLowerCase(), new ArrayList<>());
    }
    // to print the key(file name ) and (filepath)
    // public void printFileMap() {
    //     fileMap.forEach((key, fileList) -> {
    //         System.out.println("Key: " + key);
    //         fileList.forEach(file -> {
    //             System.out.println("  File: " + file.getPathName());
    //         });
    //     });
    // }

    
    
}
 