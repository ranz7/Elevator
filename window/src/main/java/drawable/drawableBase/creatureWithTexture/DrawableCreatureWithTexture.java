package drawable.drawableBase.creatureWithTexture;

import lombok.Getter;
import model.objects.movingObject.Creature;
import model.resourceLoader.GameResource;
import model.resourceLoader.ResourceLoader;
import tools.Vector2D;
import view.drawTools.GameDrawer;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class DrawableCreatureWithTexture extends DrawableCreature {
    private final static Random random = new Random();

    @Getter
    Image image;

    private final GameResource gameResource;

    public DrawableCreatureWithTexture(Vector2D position, String textureFolderName, String textureGameSourceName) {
        super(position);
        gameResource = ResourceLoader.getGameResource(textureFolderName, textureGameSourceName);
        size = gameResource.getSize();
        image = gameResource.getResourceImage();
    }

    public DrawableCreatureWithTexture(Vector2D position, String textureFolderNameToGetRandomGameSource) {
        super(position);
        int uperBound = ResourceLoader.getNumOfFilesIn(textureFolderNameToGetRandomGameSource);
        if (uperBound == 0) {
            throw new RuntimeException("There is no config(json) in:" + textureFolderNameToGetRandomGameSource);
        }
        int randomResourceNum = random.nextInt();
        var resourceName = ResourceLoader.getGameResourceNameIn(textureFolderNameToGetRandomGameSource, randomResourceNum);
        gameResource = ResourceLoader.getGameResource(textureFolderNameToGetRandomGameSource, resourceName);
        image = gameResource.getResourceImage();
    }

    @Override
    public void draw(GameDrawer gameDrawer) {
        if (isVisible) {
            gameDrawer.draw(this);
        }
    }

    @Override
    public List<Drawable> getDrawables() {
        var drawables = new LinkedList<Drawable>();
        drawables.add(this);
        return drawables;
    }

}
