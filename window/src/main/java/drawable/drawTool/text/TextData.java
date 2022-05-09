package drawable.drawTool.text;

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
        return new Trajectory().add(trajectory);
    }

    public static TextData FlyTopSlow() {
        return new TextData(7,
                new Color(220, 18, 87),
                Trajectory.ConstantSpeedInDirectionWithTimer(
                        Vector2D.North, 1500, 15));
    }

    public static TextData FlyTopFast() {
        return new TextData(7,
                new Color(220, 160, 10),
                Trajectory.ConstantSpeedInDirectionWithTimer(
                        Vector2D.North, 450, 200));
    }
}
