package view.drawTools.drawer;

import drawable.abstracts.Drawable;
import drawable.abstracts.withTexture.DrawableCreatureWithTexture;
import tools.Vector2D;
import view.drawTools.scaler.GameScaler;

import java.awt.*;

/*
 * this class transform game coordinates to normal coordinates and viceversa , so u can change
 * real coordinates and have an image streched in a normal natural way.
 */

public class GameDrawer extends BasicsDrawer {
    public GameDrawer(GameScaler gameScaler) {
        super(gameScaler);
    }

    public void draw(DrawableLocalText drawableText) {
        this.drawText(drawableText.getText(), drawableText.getPosition(),
                drawableText.getSize().x, drawableText.getColor());
    }

    public void draw(Drawable drawable, Color objectColor) {
        draw(drawable, objectColor, objectColor, 0);
    }

    public void draw(Drawable drawable, Color objectColor, Color borderColor, int thickness) {
        if (!drawable.getIsVisible()) {
            return;
        }
        Vector2D positionOfTheCreature = getShiftDrawPosition(drawable);
        this.drawFilledRect(positionOfTheCreature, drawable.getSize(), objectColor, borderColor, thickness);
    }

    public void draw(Drawable drawable, Color borderColor, double thickness) {
        if (!drawable.getIsVisible()) {
            return;
        }
        Vector2D positionOfTheCreature = getShiftDrawPosition(drawable);
        this.drawOnlyBorderRect(positionOfTheCreature, drawable.getSize(), borderColor, thickness);
    }

    public void draw(DrawableCreatureWithTexture drawableCreatureWithTexture) {
        Vector2D positionOfTheCreature = getShiftDrawPosition(drawableCreatureWithTexture);
        drawImage(drawableCreatureWithTexture.getImage(), positionOfTheCreature, drawableCreatureWithTexture.getSize());
    }

    private Vector2D getShiftDrawPosition(Drawable drawable) {
        return switch (drawable.getDrawCenter()) {
            case bottomCenter -> drawable.getPosition().getSubbed(
                    new Vector2D(drawable.getSize().x / 2., 0));
            case leftCenter -> drawable.getPosition().getSubbed(
                    new Vector2D(0, drawable.getSize().y / 2.));
            case center -> drawable.getPosition().getSubbed(
                    new Vector2D(drawable.getSize().x / 2., drawable.getSize().y / 2.));
            case bottomRight -> drawable.getPosition().getSubbed(
                    new Vector2D(drawable.getSize().x, 0));
            case bottomLeft -> drawable.getPosition().getSubbed(
                    new Vector2D(0, 0));
        };
    }
}
