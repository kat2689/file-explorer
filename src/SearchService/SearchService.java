package SearchService;
import java.util.List;




import FileInfo.FileInfo;
import Main.Main;


public class SearchService {

    public List<FileInfo> search(String query) {
        return Main.search(query);
    }
}