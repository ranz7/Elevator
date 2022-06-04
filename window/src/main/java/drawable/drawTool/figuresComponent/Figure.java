package drawable.drawTool.figuresComponent;

import drawable.drawTool.DrawTool;
import lombok.AllArgsConstructor;
import tools.Pair;
import tools.Vector2D;

import java.awt.*;

@AllArgsConstructor
public abstract class Figure extends DrawTool {
    @Override
    public void setColor(Color color) {
        mainColor = color;
    }

    @Override
    public Color getMainColor() {
        return new Color(Math.min(mainColor.getRed() + getAdditionalLightColor().getRed(), 255),
                Math.min(mainColor.getGreen() + getAdditionalLightColor().getGreen(),255),
                Math.min(mainColor.getBlue() + getAdditionalLightColor().getBlue(),255));
    }

    private Color mainColor;

    protected Pair<Vector2D, Vector2D> afterProportionApply(
            Vector2D drawPositionbeforeProportion,
            Vector2D drawSizeBeforeProportion) {
        var answer = new Pair<>(drawPositionbeforeProportion, drawSizeBeforeProportion);
        answer.getFirst().set(
                drawPositionbeforeProportion
                        .add(drawSizeBeforeProportion.multiply(0.5))
                        .sub(drawSizeBeforeProportion.multiply(positionProportion))
        );
        answer.getSecond().set(answer.getSecond().multiply(sizeProportion));
        return answer;
    }

    Vector2D positionProportion; // FROM 0 to 100 - to jest proporcja

    Vector2D sizeProportion; // FROM 0 to 100  - to jest proporcja
}
