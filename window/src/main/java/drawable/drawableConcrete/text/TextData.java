package drawable.drawableConcrete.text;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import model.objects.movingObject.trajectory.Trajectory;
import tools.Vector2D;

import java.awt.*;

@RequiredArgsConstructor
public class TextData {
    @Getter
    final int fontSize;

    @Getter
    final Color textColor;

    final Trajectory trajectory;

    public Trajectory getTrajectory() {
        return new Trajectory().set(trajectory);
    }

    public static TextData flyTopSlow = new TextData(
            4,
            new Color(12, 32, 125),
            Trajectory.ConstantSpeedInDirectionWithTimer(
                    Vector2D.North, 3000, 3));

    public static TextData flyTopFast = new TextData(
            4,
            new Color(12, 32, 125),
            Trajectory.ConstantSpeedInDirectionWithTimer(
                    Vector2D.North, 1500, 6));
}
