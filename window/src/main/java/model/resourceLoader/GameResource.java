package model.resourceLoader;

import lombok.Getter;

import java.awt.*;
import java.io.File;

public class GameResource extends File {
    @Getter
    Point size;
    ResourceImage resourceImage;

    public Image getResourceImage() {
        return resourceImage.image;
    }

    public GameResource(String absolutePath) {
        super(absolutePath);
        resourceImage = new ResourceImage(absolutePath);
    }

}
