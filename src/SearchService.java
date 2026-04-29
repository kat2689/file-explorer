import java.util.List;
import FileInfo.FileInfo;

public class SearchService {

    public List<FileInfo> search(String query) {
        return Main.search(query);
    }
}
