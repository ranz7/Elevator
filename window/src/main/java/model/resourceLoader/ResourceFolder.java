package model.resourceLoader;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class ResourceFolder extends File {
    List<GameResource> gameResources = new LinkedList<>();

    public ResourceFolder(String fullName) {
        super(fullName);
        var listOfFiles = listFiles();
        if (!exists() || listOfFiles == null) {
            throw new RuntimeException("Directory not exists" + fullName);
        }

        for (var file : listOfFiles) {
            if (file.isFile() && ResourceLoader.isJson(file.getName())) {
                gameResources.add(new GameResource(file.getAbsolutePath()));
            }
        }
    }

}
