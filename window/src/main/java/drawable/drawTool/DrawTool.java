package drawable.drawTool;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import tools.Pair;
import tools.Vector2D;
import model.planes.graphics.Painter;

import java.awt.*;


@RequiredArgsConstructor
public abstract class DrawTool {

    private final Vector2D positionProportion; // FROM 0 to 100 - to jest proporcja
    private final  Vector2D sizeProportion; // FROM 0 to 100  - to jest proporcja

    @Getter
    @Setter
    Color additionalLightColor = new Color(0, 0, 0);


    protected Pair<Vector2D, Vector2D> afterProportionApply(
            Vector2D drawPositionbeforeProportion,
            Vector2D drawSizeBeforeProportion) {
        var differenceVector =
                drawSizeBeforeProportion.sub(drawSizeBeforeProportion.multiply(sizeProportion))
                        .multiply(positionProportion);

        return new Pair<>(
                drawPositionbeforeProportion.add(differenceVector),
                drawSizeBeforeProportion.multiply(sizeProportion));
    }

    public abstract Color getMainColor();
    public abstract void draw(Vector2D position, Vector2D size, Painter drawer);
    public abstract void setColor(Color color);
    public abstract boolean isIntersect(Vector2D objectPosition, Vector2D objectSize, Vector2D gamePosition);
}
