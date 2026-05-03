package FileOpener;

import java.io.File;


public class FileOpener {

    public static void open(File file) {
        try {
            if (!file.exists()) {
                System.out.println("File not found");
                return;
            }

            ProcessBuilder pb = new ProcessBuilder(
                "cmd", "/c", "start", "", file.getAbsolutePath()
            );

            pb.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
