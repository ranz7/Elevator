package drawable.buttons;

import drawable.abstracts.DrawCenter;
import drawable.abstracts.DrawableCreature;
import drawable.drawTool.figuresComponent.Ellipse;
import drawable.drawTool.figuresComponent.FiguresComponent;
import drawable.drawTool.text.Text;
import settings.localDraw.LocalDrawSetting;
import tools.Vector2D;

import java.awt.*;


public class CircleWithTextInside extends DrawableCreature {
    public CircleWithTextInside(Vector2D position, LocalDrawSetting settings, String text) {
        super(position,
                settings.getMenuButtonSize(),
                new FiguresComponent(
                        new Ellipse(settings.getMenuButtonColor()),
                        new Text(text, new Color(0, 18, 54))),
                settings
        );
    }

    @Override
    public DrawCenter getDrawCenter() {
        return DrawCenter.center;
    }

    @Override
    public int getDrawPriority() {
        return 12;
    }
}
