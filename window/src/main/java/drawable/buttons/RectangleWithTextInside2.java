package drawable.buttons;

import drawable.abstracts.DrawCenter;
import drawable.abstracts.DrawableCreature;
import drawable.drawTool.figuresComponent.FiguresComponent;
import drawable.drawTool.figuresComponent.RectangleWithBorder;
import drawable.drawTool.text.Text;
import settings.localDraw.LocalDrawSetting;
import tools.Vector2D;
import view.buttons.MutableColor;

import java.awt.*;

public class RectangleWithTextInside2 extends DrawableCreature {
    public RectangleWithTextInside2(Vector2D position, Vector2D size, LocalDrawSetting settings, String text) {
        super(position, size,
                new FiguresComponent(
                        new RectangleWithBorder(settings.doorsColor(), new MutableColor(0, 0, 0), 1),
                         new Text(new Vector2D(0.5, 0.4),
                                 new Vector2D(0.4, 0.4), text, new MutableColor(0, 0, 0))
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
