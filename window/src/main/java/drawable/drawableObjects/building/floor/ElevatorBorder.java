package drawable.drawableObjects.building.floor;

import drawable.Drawable;
import lombok.Getter;
import model.WindowModel;
import model.objects.movingObject.Creature;
import tools.Vector2D;
import view.drawTools.GameDrawer;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class ElevatorBorder extends Creature implements Drawable {
    private Creature PARENT_ELEVATOR;
    Color BORDER_COLOR;
    Color NUMBER_COLOR;
    int BORDER_SIZE;
    Double WALL_HEIGHT;
    @Getter
    ElevatorBlackSpace elevatorBlackSpace;
    @Getter
    Button button;

    public ElevatorBorder(Vector2D position, Creature parentElevator, WindowModel windowModel) {
        super(position, parentElevator.getSize());
        PARENT_ELEVATOR = parentElevator;
        BORDER_COLOR = windowModel.COLOR_SETTINGS.BORDER_COLOR;
        NUMBER_COLOR = windowModel.COLOR_SETTINGS.NUMBER_COLOR;
        BORDER_SIZE = windowModel.DRAW_SETTINGS.BORDER_SIZE;
        WALL_HEIGHT = windowModel.getWallHeight();

        size = new Point(size.x + windowModel.DRAW_SETTINGS.BORDER_SIZE * 2,
                size.y + windowModel.DRAW_SETTINGS.BORDER_SIZE);


        elevatorBlackSpace = new ElevatorBlackSpace(position, parentElevator, windowModel);
        button = new Button(position.getAdded(new Vector2D(size).getDivided(2)).getAdded(
                new Vector2D(windowModel.getSettings().BUTTON_RELATIVE_POSITION, 0)), windowModel);
    }

    @Override
    public Integer GetDrawPrioritet() {
        return 9;
    }

    @Override
    public void draw(GameDrawer gameDrawer) {
        if (!PARENT_ELEVATOR.isVisible()) {
            return;
        }
        gameDrawer.setColor(BORDER_COLOR);
        gameDrawer.drawRect(this.position, this.size, BORDER_SIZE * 2);
        gameDrawer.setFont("TimesRoman", Font.PLAIN, 15);
        var positionOfText = position;
        gameDrawer.setColor(NUMBER_COLOR);
        gameDrawer.drawString(
                ((int) (PARENT_ELEVATOR.getPosition().y / WALL_HEIGHT + 1)) + "", positionOfText.getAdded(
                        new Vector2D(-2, size.y - BORDER_SIZE + 3)));
    }

    @Override
    public List<Drawable> getDrawables() {
        var drawables = new LinkedList<Drawable>();
        drawables.add(this);
        drawables.add(elevatorBlackSpace);
        drawables.add(button);
        return drawables;
    }

    public void tick(long delta_time) {
    }

}
