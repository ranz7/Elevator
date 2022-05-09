package drawable.drawTool;

import configs.tools.CombienedDrawDataBase;
import drawable.drawTool.figuresComponent.Figure;
import lombok.Getter;
import model.objects.Creature;
import model.resourceLoader.GameResource;
import model.resourceLoader.ResourceLoader;
import view.drawTools.drawer.GameDrawer;

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
    public void draw(GameDrawer drawer, Creature creature) {
        // CO MAM TUTAJ ROBIC
    }
}
