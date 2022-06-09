package view.buttons;

import drawable.abstracts.DrawCenter;
import drawable.abstracts.Drawable;
import drawable.abstracts.DrawableCreature;
import drawable.buttons.ClickableButton;
import drawable.buttons.MovingSquareWithTextInside;
import drawable.concretes.game.floor.DrawableFloorStructure;
import drawable.concretes.menu.Portal;
import drawable.drawTool.figuresComponent.Rectangle;
import lombok.Getter;
import lombok.Setter;
import model.DatabaseOf;
import model.Transport;
import settings.localDraw.LocalDrawSetting;
import tools.Timer;
import tools.Trio;
import tools.Vector2D;

import java.awt.*;
import java.util.LinkedList;

public class GameButtonComponent extends DrawableCreature implements Transport<Drawable> {
    @Getter
    private final  DatabaseOf<Drawable> localDataBase = new DatabaseOf<>(this, ClickableButton.class);

    private final LinkedList<ClickableButton> automat = new LinkedList<>();

    private final Timer timerToShoot = new Timer(200);
    private final Portal portal;

    public GameButtonComponent(Portal portal, LocalDrawSetting setting) {
        super(new Vector2D(15, 15), new Vector2D(40, 40), new Rectangle(Color.cyan), setting);
        this.portal = portal;
        var start = portal.getGamePlane().getGameMap().getBuildingSize().divide(2);
        automat.add(new ClickableButton(
                new MovingSquareWithTextInside(start,
                        portal.getGamePlane().getGameMap().getBuildingSize().sub(new Vector2D(45, 45)),
                        new Vector2D(20, 20),
                        "x",
                        getSettings()), this::destroy
        ));

        automat.add(new ClickableButton(
                new MovingSquareWithTextInside(start,
                        portal.getGamePlane().getGameMap().getBuildingSize().sub(new Vector2D(105, 45)),
                        new Vector2D(40, 20),
                        ">>",
                        getSettings()),() -> portal
                .getPlane()
                .getGameMap()
                .changeGameSpeed(false)
        ));


        portal.getGamePlane()
                .getGameMap()
                .getLocalDataBase()
                .streamTrio()
                .map(Trio::getSecondAndThird)
                .filter(pair -> pair.getSecond() instanceof DrawableFloorStructure)
                .forEach(
                        floor -> {
                            automat.add(new ClickableButton(
                                    new MovingSquareWithTextInside(start,
                                            floor.getFirst().add(
                                                    floor.getSecond()
                                                            .getPosition()),
                                            new Vector2D(50, 20),
                                            "->",
                                            getSettings()), () -> portal
                                    .getPlane()
                                    .getGameMap().createCustomer(floor.getSecond().getId(), true)
                            ));
                            automat.add(new ClickableButton(
                                    new MovingSquareWithTextInside(
                                            start,
                                            floor.getFirst().add(floor.getSecond().getPosition())
                                                    .withX(floor
                                                            .getSecond()
                                                            .getSize().x - 50),
                                            new Vector2D(50, 20),
                                            "<-",
                                            getSettings()), () -> portal
                                    .getPlane()
                                    .getGameMap()
                                    .createCustomer(floor.getSecond().getId(), false)
                            ));
                        }
                );

        automat.add(new ClickableButton(
                new MovingSquareWithTextInside(start,
                        portal.getGamePlane().getGameMap().getBuildingSize().sub(new Vector2D(175, 45)),
                        new Vector2D(40, 20),
                        "<<",
                        getSettings()),() -> portal
                .getPlane()
                .getGameMap()
                .changeGameSpeed(true)
        ));


    }

    Timer deadTimer = new Timer(2000);

    @Override
    public void tick(double deltaTime) {
        super.tick(deltaTime);
        timerToShoot.tick(deltaTime);
        deadTimer.tick(deltaTime);
        if (timerToShoot.isReady()) {
            timerToShoot.restart();
            if (!automat.isEmpty()) {
                localDataBase.addCreature(automat.getLast());
                automat.removeLast();
            }
        }
        if (destroy) {
            localDataBase.streamTrio().map(Trio::getThird).filter(object -> object != this).forEach(
                    button -> ((MovingSquareWithTextInside) ((ClickableButton) button).getParasite()).destroy()
            );
            if (deadTimer.isReady()) {
                setDead(true);
                portal.changeZoom();
            }
        }
    }

    @Override
    public void add(Drawable drawableCreature) {
        localDataBase.addCreature(drawableCreature);
    }

    boolean destroy = false;

    public void destroy() {
        destroy = true;
        deadTimer.restart();
        // start to remove all buttons
        // then set to deat true
    }

    @Override
    public DrawCenter getDrawCenter() {
        return DrawCenter.center;
    }

    @Override
    public int getDrawPriority() {
        return 0;
    }

}
