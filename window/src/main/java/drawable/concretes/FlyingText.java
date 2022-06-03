package drawable.concretes;

import drawable.abstracts.DrawCenter;
import drawable.abstracts.DrawableMovingCreature;
import drawable.drawTool.text.Text;
import model.objects.movingObject.MovingCreature;
import model.objects.movingObject.trajectory.Trajectory;
import tools.Vector2D;

import java.awt.*;

public class FlyingText extends DrawableMovingCreature {
    public static FlyingText FlyingTextUpSlow(String text, Vector2D position) {
        return new FlyingText(
                text, position,
                Trajectory.ConstantSpeedInDirectionWithTimer(Vector2D.Up, 1500, 15),
                7, new Color(220, 18, 87));
    }

    public static FlyingText FlyingTextUpFast(String text, Vector2D position) {
        return new FlyingText(text, position,
                Trajectory.ConstantSpeedInDirectionWithTimer(Vector2D.Up, 450, 200),
                7, new Color(220, 160, 10));
    }

    public FlyingText(String text, Vector2D position, Trajectory trajecotry, int colorSize, Color color) { // uups :)
        super( position, new Vector2D(100, colorSize), trajecotry, new Text(text, color), null);
    }


    @Override
    public DrawCenter getDrawCenter() {
        return DrawCenter.bottomLeft;
    }

    @Override
    public int getDrawPriority() {
        return 20;
    }
}
