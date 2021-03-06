package model.resourceLoader;

import lombok.Getter;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import tools.Vector2D;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class GameResource extends File {
    @Getter
    Vector2D size;
    ResourceImage resourceImage;

    public Image getResourceImage() {
        return resourceImage.image;
    }

    public GameResource(String jsonAbsolutePath) {
        super(jsonAbsolutePath);

        String str = null;
        try {
            str = FileUtils.readFileToString(this, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject obj = new JSONObject(str);
        size = new Vector2D(obj.getInt("width"), obj.getInt("height"));
        var imageExtension = obj.getString("imageExtension");

        resourceImage = new ResourceImage(ResourceLoader.changeExtension(jsonAbsolutePath, imageExtension));
    }

}
