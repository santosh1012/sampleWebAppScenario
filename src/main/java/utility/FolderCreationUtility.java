package utility;

import java.io.File;

public class FolderCreationUtility {

    public static void createFolderIfNotExist(String folderPath) {
        File files = new File(folderPath);
        if (!files.exists()) {
            if (files.mkdirs()) {
                System.out.println("Multiple directories are created!");
            } else {
                System.out.println("Failed to create multiple directories!");
            }
        }
    }
}
