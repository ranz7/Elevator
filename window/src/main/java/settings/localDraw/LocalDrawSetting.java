package settings.localDraw;

import tools.DataBase;
import tools.Vector2D;

import java.awt.*;
import java.util.Random;

public class LocalDrawSetting implements DataBase {
    private final DrawConfig drawConfig = new DrawConfig();
    private final ColorConfig colorConfig = new ColorConfig();

    public Color getRandomCustomerSkin() {
        var colors = customerSkins();
        return colors[(int) ((Math.abs(new Random().nextInt())) % colors.length)];
    }
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
        return colorConfig.elevatorBorder;
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

    public double customerWidth() {
        return 40;
    }

    public Vector2D getMenuButtonSize() {
        return drawConfig.menuButtonSize;
    }

    public Color getMenuButtonColor() {
        return colorConfig.menuButtonColor;
    }

    public Vector2D portalSize() {
        return drawConfig.portalSize;
    }
}
