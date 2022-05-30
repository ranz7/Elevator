package drawable.concretes.menu;

import drawable.abstracts.DrawCenter;
import drawable.abstracts.DrawableCreature;
import drawable.drawTool.figuresComponent.Rectangle;
import lombok.Getter;
import model.DatabaseOf;
import model.Transport;
import settings.localDraw.LocalDrawSetting;
import tools.Vector2D;



public class MenuDrawable extends DrawableCreature implements Transport {

    @Getter
    DatabaseOf<DrawableCreature> localDataBase = new DatabaseOf<>(this);

    public MenuDrawable(LocalDrawSetting localDrawSetting) {
        super(new Vector2D(0, 0), new Vector2D(0, 0), new Rectangle(null), localDrawSetting);
    }

    @Override
    public DrawCenter getDrawCenter() {
        return DrawCenter.bottomLeft;
    }

    @Override
    public int GetDrawPrioritet() {
        return 10;
    }
}
