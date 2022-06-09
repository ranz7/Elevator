package settings.localDraw;

import tools.DataBase;
import tools.Vector2D;
import view.buttons.MutableColor;

import java.awt.*;
import java.util.Random;

public class LocalDrawSetting implements DataBase {
    private final DrawConfig drawConfig = new DrawConfig();
    private final ColorConfig colorConfig = new ColorConfig();

    public MutableColor getRandomCustomerSkin() {
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

    public MutableColor buttonColorOn() {
        return colorConfig.elevatorButtonOn;
    }

    public MutableColor buttonColorOff() {
        return colorConfig.elevatorButtonOff;
    }

    public long buttonOnTime() {
        return drawConfig.elevatorButtonOnTime;
    }

    public MutableColor doorsColor() {
        return colorConfig.elevatorDoor;
    }

    public MutableColor doorsBorder() {
        return colorConfig.elevatorBorder;
    }

    public MutableColor[] customerSkins() {
        return colorConfig.customersSkin;
    }

    public MutableColor florBetonColor() {
        return colorConfig.betonOfFloor;
    }

    public MutableColor backGroundColor() {
        return colorConfig.backGroundColor;
    }

    public MutableColor floorWallColor() {
        return colorConfig.floorWall;
    }

    public double borderThickness() {
        return drawConfig.elevatorBorderThickness;
    }

    public MutableColor borderColor() {
        return colorConfig.elevatorBorder;
    }

    public MutableColor colorOfNumber() {
        return colorConfig.elevatorFloorNumber;
    }

    public MutableColor elevatorBackGroundColor() {
        return colorConfig.elevatorBackGround;
    }

    public MutableColor jButtonsColor() {
        return colorConfig.jButtonsColor;
    }

    public double customerWidth() {
        return 40;
    }

    public Vector2D getMenuButtonSize() {
        return drawConfig.menuButtonSize;
    }

    public MutableColor getMenuButtonColor() {
        return colorConfig.menuButtonColor;
    }

    public Vector2D portalSize() {
        return drawConfig.portalSize;
    }

    public MutableColor portalColor() {
        return colorConfig.portalColor;
    }

    public MutableColor blackSpacesColor() {
        return colorConfig.blackSpacesColor;
    }

    Color randomColor(Random rand) {
        return new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
    }

    public void colorRandom() {
        var rand = new Random();
        colorConfig.backGroundColor.setColor(randomColor(rand));
        colorConfig.betonOfFloor.setColor(randomColor(rand));
        colorConfig.blackSpacesColor.setColor(randomColor(rand));
        colorConfig.elevatorDoor.setColor(randomColor(rand));
        colorConfig.portalColor.setColor(randomColor(rand));
        colorConfig.elevatorBackGround.setColor(randomColor(rand));
        colorConfig.elevatorBorder.setColor(randomColor(rand));
        colorConfig.elevatorButtonOff.setColor(randomColor(rand));
        colorConfig.elevatorButtonOn.setColor(randomColor(rand));
        colorConfig.menuButtonColor.setColor(randomColor(rand));
        colorConfig.floorWall.setColor(randomColor(rand));
        colorConfig.elevatorFloorNumber.setColor(randomColor(rand));
        colorConfig.elevatorBackGround.setColor(randomColor(rand));
    }
}
