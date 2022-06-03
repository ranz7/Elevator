package drawable.abstracts;

import lombok.Getter;
import lombok.Setter;

import model.objects.movingObject.trajectory.Trajectory;
import settings.localDraw.LocalDrawSetting;
import drawable.drawTool.DrawTool;
import model.objects.movingObject.MovingCreature;
import tools.Vector2D;

public abstract class DrawableMovingCreature extends MovingCreature implements Drawable {
    @Getter
    final private DrawTool tool;
    @Getter
    final private LocalDrawSetting settings;

    @Getter
    @Setter
    Vector2D realDrawPosition;

    public DrawableMovingCreature(Vector2D position, Vector2D size, Trajectory trajecotry, DrawTool tool, LocalDrawSetting settings) {
        super(position, size, trajecotry);
        this.tool = tool;
        this.settings = settings;
        this.id = -1; // local objects does not have id
    }
}
