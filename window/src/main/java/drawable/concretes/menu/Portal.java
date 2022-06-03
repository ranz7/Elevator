package drawable.concretes.menu;

import drawable.abstracts.DrawCenter;
import drawable.abstracts.Drawable;
import drawable.abstracts.DrawableCreature;
import drawable.buttons.CircleWithTextInside;
import drawable.buttons.ClickableButton;
import drawable.drawTool.figuresComponent.Rectangle;
import lombok.Getter;
import lombok.Setter;
import model.DatabaseOf;
import model.Transport;
import model.planes.GamePlane;
import settings.localDraw.LocalDrawSetting;
import tools.Vector2D;

import java.awt.*;

public class Portal extends DrawableCreature implements Transport<Drawable> {
    @Getter
    DatabaseOf<Drawable> localDataBase = new DatabaseOf<>(this, ClickableButton.class);

    @Setter
    private GamePlane gamePlane;

    @Getter
    private int roomId = -1;

    public Portal(double positionX, LocalDrawSetting settings) {
        super(new Vector2D(positionX, 0), settings.portalSize(), new Rectangle(new Color(0, 139, 203)), settings);
        add(new ClickableButton(
                new CircleWithTextInside(getSize().divide(new Vector2D(-5, 2)), settings)));
        add(new ClickableButton(
                new CircleWithTextInside(getSize().divide(new Vector2D(-5, 2)).addByY(5), settings)));
        add(new ClickableButton(
                new CircleWithTextInside(getSize().divide(new Vector2D(-5, 2)).addByY(-5), settings)));
        roomId = 0;
    }

    @Override
    public void add(Drawable creature) {
        localDataBase.addCreature(creature);
    }

    @Override
    public DrawCenter getDrawCenter() {
        return DrawCenter.bottomLeft;
    }

    @Override
    public int getDrawPriority() {
        return -9;
    }

    @Override
    public void tick(double deltaTime) {
        super.tick(deltaTime);
        if (gamePlane != null)
            System.out.println(gamePlane.getRoomRemoteSettings().roomId());
    }
}

