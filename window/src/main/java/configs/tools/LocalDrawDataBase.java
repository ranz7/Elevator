package configs.tools;

import configs.canvas.ColorConfig;
import configs.canvas.DrawConfig;
import lombok.RequiredArgsConstructor;
import tools.DataBase;
import tools.Vector2D;

import java.awt.*;

@RequiredArgsConstructor
public class LocalDrawDataBase implements DataBase {
    private final ColorConfig colorConfig;
    private final DrawConfig drawConfig;

    @Override
    public boolean initialized(Class<?>... classes) {
        return true;
    }

    public Vector2D elevatorButtonSize() {
        return drawConfig.elevatorButtonSize;
    }

    public Color buttonColorOn() {
        return colorConfig.elevatorButtonOn;
    }

    public Color buttonColorOff() {
        return colorConfig.elevatorButtonOff;
    }

    public long buttonOnTime() {
        return drawConfig.elevatorButtonOnTime;
    }

    public Color doorsColor() {

        return colorConfig.elevatorDoor;
    }

    public Color doorsBorder() {
        return colorConfig.elevatorDoor;
    }

    public Color[] customerSkins() {
        return colorConfig.customersSkin;
    }

    public Color florBetonColor() {
        return colorConfig.betonOfFloor;
    }

    public Color backGroundColor() {
        return colorConfig.backGroundColor;
    }

    public Color floorWallColor() {
        return colorConfig.floorWall;
    }

    public double borderThickness() {

        return drawConfig.elevatorBorderThickness;
    }

    public Color borderColor() {
        return colorConfig.elevatorBorder;
    }

    public Color colorOfNumber() {
        return colorConfig.elevatorFloorNumber;
    }

    public Color elevatorBackGroundColor() {
        return colorConfig.elevatorBackGround;
    }

    public Color jButtonsColor() {
        return colorConfig.jButtonsColor;
    }

    public long picturesGeneratorSeed() {
        return drawConfig.picturesGeneratorSeed;
    }
}
