package drawable.buttons;

import drawable.RainbowColor;
import drawable.abstracts.DrawCenter;
import drawable.abstracts.DrawableMovingCreature;
import drawable.drawTool.figuresComponent.FiguresComponent;
import drawable.drawTool.figuresComponent.Rectangle;
import drawable.drawTool.text.Text;
import model.objects.movingObject.trajectory.MoveFunction;
import model.objects.movingObject.trajectory.SpeedFunction;
import model.objects.movingObject.trajectory.Trajectory;
import model.planes.graphics.Painter;
import settings.localDraw.LocalDrawSetting;
import tools.Vector2D;

import java.awt.*;

public class MovingSquareWithTextInside extends DrawableMovingCreature {
    RainbowColor rainbow;
    Vector2D start;

    public MovingSquareWithTextInside(Vector2D position,
                                      Vector2D positionEnd,
                                      Vector2D size,
                                      String text,
                                      LocalDrawSetting settings) {
        super(position, size,
                new Trajectory()
                        .add(SpeedFunction.WithConstantSpeed(() -> 300.))
                        .add(MoveFunction.GetToDestination(() -> positionEnd)),
                new FiguresComponent(
                        new Rectangle(settings.getMenuButtonColor()),
                        new Text(text, new Color(0, 18, 54))),
                settings
        );
        start = new Vector2D(getPosition());
        rainbow = new RainbowColor(settings.getMenuButtonColor());
    }

    @Override
    public void tick(double delta_time) {
        super.tick(delta_time);
        getTool().setColor(rainbow.next());
    }

    @Override
    public void draw(Vector2D realDrawPosition, Painter gameDrawer) {
        getTool().setColor(rainbow.next());
        super.draw(realDrawPosition, gameDrawer);
    }

    @Override
    public DrawCenter getDrawCenter() {
        return DrawCenter.center;
    }

    @Override
    public boolean isDead() {
        if (getSize().length() < 0.1) {
            return true;
        }
        return isReachedDestination();
    }

    @Override
    public int getDrawPriority() {
        return 55;
    }

    boolean destroy = false;

    public void destroy() {
        setSize(getSize().multiply(0.98));
        if (destroy == false) {
            setSize(getSize().multiply(1.5));
            setMoveTrajectory(
                    new Trajectory()
                            .add(SpeedFunction.WithConstantSpeed(() -> 400.))
                            .add(MoveFunction.InDirectionAndTimer(
                                    start.sub(getPosition())
                                            .divide(start.sub(getPosition())
                                                    .length()), 3)));
            destroy = true;
        }
    }
}
