package drawable.drawableBase.drawableWithTexture;

import drawable.drawableBase.Drawable;
import drawable.drawableBase.DrawableCreature;
import lombok.Getter;
import model.resourceLoader.GameResource;
import model.resourceLoader.ResourceLoader;
import tools.Vector2D;
import view.drawTools.GameDrawer;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class DrawableWithTexture extends DrawableCreature {
    private final static Random random = new Random();

    @Getter
    Image image;

    private final GameResource gameResource;

    public DrawableWithTexture(Vector2D position, String textureFolderName, String textureGameSourceName) {
        super(position,new Vector2D());
        gameResource = ResourceLoader.getGameResource(textureFolderName, textureGameSourceName);
        size = gameResource.getSize();
        image = gameResource.getResourceImage();
    }

    public DrawableWithTexture(Vector2D position, String textureFolderNameToGetRandomGameSource) {
        this(position, textureFolderNameToGetRandomGameSource, getRandomGameSourceFrom(textureFolderNameToGetRandomGameSource));
    }

    private static String getRandomGameSourceFrom(String textureFolderNameToGetRandomGameSource) {
        int uperBound = ResourceLoader.getNumOfFilesIn(textureFolderNameToGetRandomGameSource);
        if (uperBound == 0) {
            throw new RuntimeException("There is no config(json) in:" + textureFolderNameToGetRandomGameSource);
        }
        int randomResourceNum = random.nextInt(uperBound);
        return ResourceLoader.getGameResourceNameIn(textureFolderNameToGetRandomGameSource, randomResourceNum);
    }

    @Override
    public void draw(GameDrawer gameDrawer) {
        gameDrawer.draw(this);
    }

    @Override
    public Integer GetDrawPrioritet() {
        return 14;
    }

    @Override
    public List<Drawable> getDrawables() {
        var drawables = new LinkedList<Drawable>();
        drawables.add(this);
        return drawables;
    }


}
