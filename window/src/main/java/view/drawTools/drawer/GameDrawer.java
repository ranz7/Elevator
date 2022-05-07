package view.drawTools.drawer;

import drawable.drawableAbstract.Drawable;
import drawable.drawableAbstract.drawableWithTexture.DrawableCreatureWithTexture;
import drawable.drawableObjectsConcrete.text.DrawableLocalText;
import tools.Vector2D;
import view.drawTools.scaler.GameScaler;

import java.awt.*;

/*
 * this class transform game coordinates to normal coordinates and viceversa , so u can change
 * real coordinates and have an image streched in a normal natural way.
 */

public class GameDrawer extends ShapeDrawer {
    public GameDrawer(GameScaler gameScaler) {
        super(gameScaler);
    }

    public void draw(DrawableLocalText drawableText) {
        this.drawText(drawableText.getText(), drawableText.getPosition(),
                drawableText.getSize().x, drawableText.getColor());
    }

    public void draw(Drawable drawable) {
        if (!drawable.getIsVisible()) {
            return;
        }
        Vector2D positionOfTheCreature = getPositionCenteredBySettings(drawable);
        this.fillRect(positionOfTheCreature, drawable.getSize());
    }


    public void draw(DrawableCreatureWithTexture drawableCreatureWithTexture) {
        Vector2D positionOfTheCreature = getPositionCenteredBySettings(drawableCreatureWithTexture);
        graphics2D.drawImage(drawableCreatureWithTexture.getImage(),
                (int) gameScaler.getFromGameToRealCoordinate(positionOfTheCreature, drawableCreatureWithTexture.getSize().y).x,
                (int) gameScaler.getFromGameToRealCoordinate(positionOfTheCreature, drawableCreatureWithTexture.getSize().y).y,
                (int) gameScaler.getFromRealToGameLength(drawableCreatureWithTexture.getSize().x),
                (int) gameScaler.getFromRealToGameLength(drawableCreatureWithTexture.getSize().y),
                null);
    }

    private Vector2D getPositionCenteredBySettings(Drawable drawable) {
        return switch (drawable.getDrawCenter()) {
            case CENTER_BY_X -> drawable.getPosition().getSubbed(
                    new Vector2D(drawable.getSize().x / 2., 0));
            case CENTER_BY_Y -> drawable.getPosition().getSubbed(
                    new Vector2D(0, drawable.getSize().y / 2.));
            case CENTER_BY_XY -> drawable.getPosition().getSubbed(
                    new Vector2D(drawable.getSize().x / 2., drawable.getSize().y / 2.));
        };
    }

    public void drawWithBorder(Drawable drawable, Color colorOfTheBorder, double thicknes) {
        if (!drawable.getIsVisible()) {
            return;
        }
        Vector2D positionOfTheCreature = getPositionCenteredBySettings(drawable);
        this.drawRect(
                positionOfTheCreature,
                drawable.getSize(),
                colorOfTheBorder,
                thicknes
        );
    }
}
