package drawable.buttons;

import drawable.abstracts.DrawCenter;
import drawable.abstracts.DrawableCreature;
import drawable.drawTool.DrawTool;
import drawable.drawTool.figuresComponent.Ellipse;
import settings.localDraw.LocalDrawSetting;
import tools.Vector2D;

import java.awt.*;

public class CircleWithTextInside extends DrawableCreature {
    public CircleWithTextInside(Vector2D position, LocalDrawSetting settings) {
        super(position,
                settings.getMenuButtonSize()
                , new Ellipse(settings.getMenuButtonColor()),
                settings
        );
    }

    @Override
    public DrawCenter getDrawCenter() {
        return DrawCenter.center;
    }

    @Override
    public int getDrawPrioritet() {
        return 12;
    }
}
