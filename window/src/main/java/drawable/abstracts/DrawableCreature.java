package drawable.abstracts;

import lombok.Setter;
import settings.localDraw.LocalDrawSetting;
import drawable.drawTool.DrawTool;
import lombok.Getter;
import model.objects.Creature;
import tools.Vector2D;

public abstract class DrawableCreature extends Creature implements Drawable {
    @Getter
    final private DrawTool tool;
    @Getter
    final private LocalDrawSetting settings;

    @Getter
    @Setter
    Vector2D realDrawPosition = new Vector2D(200,200);

    public DrawableCreature(Vector2D position, Vector2D size, DrawTool tool, LocalDrawSetting settings) {
        super(position, size);
        this.tool = tool;
        this.settings = settings;
        this.id = -1; // local objects does not have id
    }

}
