package drawable.buttons;

import drawable.abstracts.DrawCenter;
import drawable.abstracts.DrawableCreature;
import drawable.drawTool.figuresComponent.FiguresComponent;
import drawable.drawTool.figuresComponent.Rectangle;
import drawable.drawTool.figuresComponent.RectangleWithBorder;
import drawable.drawTool.text.Text;
import settings.localDraw.LocalDrawSetting;
import tools.Vector2D;

import java.awt.*;

public class RectangleWithTextInside extends DrawableCreature {
    public RectangleWithTextInside(Vector2D position, Vector2D size, LocalDrawSetting settings, String text) {
        super(position, size,
                new FiguresComponent(
                        new RectangleWithBorder(settings.doorsColor(), new Color(0, 0, 0), 1),
                         new Text(new Vector2D(0.5, 0.5), new Vector2D(0.1, 0.05), text, new Color(49, 98, 190))
                ),
                settings
        );
    }

    @Override
    public DrawCenter getDrawCenter() {
        return DrawCenter.center;
    }

    @Override
    public int getDrawPriority() {
        return 998;
    }
}
