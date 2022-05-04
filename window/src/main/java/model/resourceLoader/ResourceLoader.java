package model.resourceLoader;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class ResourceLoader {
    static private final List<ResourceFolder> folders = new LinkedList<>();

    static {
        String absoluteDirPath = getFromRelativeAbsolute("/images/");

        File resourceFolder = new File(absoluteDirPath);
        if (!resourceFolder.exists()) {
            throw new RuntimeException("Resource folder does not exists: " + resourceFolder.getPath());
        }
        addAllDirsFromFolder(resourceFolder);
    }

    public static String getFromRelativeAbsolute(String relativePath) {
        var absolutePath = ResourceLoader.class.getResource(relativePath).getPath();
        if (absolutePath == null) {
            throw new RuntimeException("unexisting relative path" + relativePath);
        }
        return absolutePath;
    }

    private static void addAllDirsFromFolder(File currentFolderAbsolute) {
        folders.add(new ResourceFolder(currentFolderAbsolute.getAbsolutePath()));
        File[] foldersToCheck = currentFolderAbsolute.listFiles(File::isDirectory);
        assert foldersToCheck != null;
        for (var folder : foldersToCheck) {
            addAllDirsFromFolder(folder);
        }
    }

    public static int getNumOfFilesIn(String folderNameRelative) {
        var absolutePath = ResourceLoader.getFromRelativeAbsolute(folderNameRelative);
        ResourceFolder folder = saveFindFolder(absolutePath);
        return folder.gameResources.size();
    }


    public static GameResource getGameResource(String folderNameRelative, String resourceName) {
        var absolutePath = ResourceLoader.getFromRelativeAbsolute(folderNameRelative);
        GameResource file = saveFindGameResourceInFolder(absolutePath, resourceName);
        return file;
    }


    public static String getGameResourceNameIn(String folderNameRelative, int resourceNumber) {
        var absolutePath = ResourceLoader.getFromRelativeAbsolute(folderNameRelative);
        ResourceFolder folder = saveFindFolder(absolutePath);
        return folder.gameResources.get(resourceNumber).getName();
    }

    private static ResourceFolder saveFindFolder(String folderNameAbsolute) {
        var ref = new Object() {
            ResourceFolder folder;
        };
        folders.stream()
                .filter(resourceFolder -> pathsAreEqual(resourceFolder.getAbsolutePath(), folderNameAbsolute))
                .findFirst().ifPresentOrElse(resourceFolder -> {
                    ref.folder = resourceFolder;
                }, () -> {
                    throw new RuntimeException("No such folder: " + folderNameAbsolute);
                });
        return ref.folder;
    }

    private static boolean pathsAreEqual(String absolutePathA, String absolutePathB) {
        File A = new File(absolutePathA);
        File B = new File(absolutePathB);
        return A.getAbsolutePath().equals(B.getAbsolutePath());
    }

    private static GameResource saveFindGameResourceInFolder(String folderNameAbsolute, String resourceName) {
        var ref = new Object() {
            GameResource file;
        };
        ResourceFolder folder = saveFindFolder(folderNameAbsolute);
        folder.gameResources.stream()
                .filter(gameResources -> gameResources.getName().equals(resourceName))
                .findFirst().ifPresentOrElse(resourceFolder -> {
                    ref.file = resourceFolder;
                }, () -> {
                    throw new RuntimeException("No such resource: " + resourceName);
                });
        return ref.file;
    }


    public static boolean isJson(String name) {
        return name.substring(name.lastIndexOf(".") + 1).equals("json");
    }

    public static String changeExtension(String name, String newExtensionWithDot) {
        return name.substring(0, name.lastIndexOf(".") + 1) + newExtensionWithDot;
    }

}
