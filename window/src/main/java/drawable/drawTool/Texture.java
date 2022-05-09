package drawable.drawTool;

import model.resourceLoader.GameResource;
import model.resourceLoader.ResourceLoader;
import tools.Vector2D;
import view.graphics.GameGraphics;

import java.awt.*;
import java.util.Random;

public class Texture extends DrawTool {
    private final GameResource gameResource;

    public Texture(String textureFolderName, String textureGameSourceName) {
        gameResource = ResourceLoader.getGameResource(textureFolderName, textureGameSourceName);
    }

    public Texture(String textureFolderNameToGetRandomGameSource, Random random) {
        this(textureFolderNameToGetRandomGameSource,
                getRandomGameSourceFrom(textureFolderNameToGetRandomGameSource, random));
    }

    private static String getRandomGameSourceFrom(String textureFolderNameToGetRandomGameSource, Random random) {
        int upperBound = ResourceLoader.getNumOfFilesIn(textureFolderNameToGetRandomGameSource);
        if (upperBound == 0) {
            throw new RuntimeException("There is no config(json) in:" + textureFolderNameToGetRandomGameSource);
        }
        int randomResourceNum = random.nextInt(upperBound);
        return ResourceLoader.getGameResourceNameIn(textureFolderNameToGetRandomGameSource, randomResourceNum);
    }

    @Override
    public void draw(Vector2D position, Vector2D size, GameGraphics drawer) {
        // TODO make stretch of the image with size , for example size 50  50 mieans that image is smaller 2 times
        drawer.drawImage(gameResource.getResourceImage(),position, gameResource.getSize());
    }

    @Override
    public void setColor(Color color) {
        // HA LOL WTH
    }

}
