package drawable.drawTool.text;

import drawable.drawTool.DrawTool;
import model.objects.Creature;
import view.drawTools.drawer.GameDrawer;

import java.awt.*;

public class Text extends DrawTool {
    private  String text;
    private  Color textColor;

    public Text(String text,TextData data) {
        this.text = text;
        this.textColor = data.getTextColor();
    }

    @Override
    public void draw(GameDrawer drawer, Creature creature) {

    }
/*
    @Override
    public Integer GetDrawPrioritet() {
        return 16;
    }

    @Override
    public void tick(double deltaTime) {
        super.tick(deltaTime);
    }

    public Color getColor() {
        return textColor;
    }

    @Override
    public boolean isDead() {
        return isReachedDestination();
    }

    */
}
