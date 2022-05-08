package drawable.drawableAbstract.drawableWithTexture;

import configs.tools.CombienedDrawDataBase;
import drawable.drawableAbstract.Drawable;
import drawable.drawableAbstract.DrawableLocalCreature;
import lombok.Getter;
import model.resourceLoader.GameResource;
import model.resourceLoader.ResourceLoader;
import tools.Vector2D;
import view.drawTools.drawer.GameDrawer;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class DrawableCreatureWithTexture extends DrawableLocalCreature {
    @Getter
    Image image;

    private final GameResource gameResource;

    public DrawableCreatureWithTexture(Vector2D position, String textureFolderName, String textureGameSourceName, CombienedDrawDataBase settings) {
        super(position, ResourceLoader.getGameResource(textureFolderName, textureGameSourceName).getSize(), settings);
        gameResource = ResourceLoader.getGameResource(textureFolderName, textureGameSourceName);
        image = gameResource.getResourceImage();
    }

    public DrawableCreatureWithTexture(Vector2D position, String textureFolderNameToGetRandomGameSource,
                                       CombienedDrawDataBase settings, Random random) {
        this(position, textureFolderNameToGetRandomGameSource,
                getRandomGameSourceFrom(textureFolderNameToGetRandomGameSource, random), settings);
    }

    private static String getRandomGameSourceFrom(String textureFolderNameToGetRandomGameSource, Random random) {
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
