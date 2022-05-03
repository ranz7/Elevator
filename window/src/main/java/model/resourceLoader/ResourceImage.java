package model.resourceLoader;

import java.awt.*;
import java.io.File;

public class ResourceImage extends File {
    Image image;
    public ResourceImage(String imageFullPath) {
        super(imageFullPath);
        if (!exists()) {
            throw new RuntimeException("Image not exists " + imageFullPath);
        }
        this.image = Toolkit.getDefaultToolkit().createImage(imageFullPath);
    }
}
