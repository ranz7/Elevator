package drawable.abstracts.withTexture;

import configs.tools.CombienedDrawDataBase;
import drawable.abstracts.Drawable;
import drawable.abstracts.withShape.creatures.DrawableLocalCreature;
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
    @Override
    public void draw(GameDrawer gameDrawer) {
        gameDrawer.draw(this);
    }

    @Override
    public Integer GetDrawPrioritet() {
        return 14;
    }

}
