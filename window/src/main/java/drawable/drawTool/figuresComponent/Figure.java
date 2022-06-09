package drawable.drawTool.figuresComponent;

import drawable.drawTool.DrawTool;
import lombok.AllArgsConstructor;
import tools.Pair;
import tools.Vector2D;
import view.buttons.MutableColor;

import java.awt.*;

public abstract class Figure extends DrawTool {
    private MutableColor mainColor;

    public Figure(MutableColor mainColor, Vector2D positionProportion, Vector2D sizeProportion) {
        super(positionProportion, sizeProportion);
        this.mainColor = mainColor;
    }

    @Override
    public void setColor(Color color) {
        mainColor = new MutableColor(color);
    }

    @Override
    public MutableColor getMainColor() {
        return new MutableColor(new Color(Math.min(mainColor.getRed() + getAdditionalLightColor().getRed(), 255),
                Math.min(mainColor.getGreen() + getAdditionalLightColor().getGreen(), 255),
                Math.min(mainColor.getBlue() + getAdditionalLightColor().getBlue(), 255)));
    }

}
