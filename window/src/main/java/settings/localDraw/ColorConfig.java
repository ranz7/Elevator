package settings.localDraw;

import view.buttons.MutableColor;

import java.awt.*;

class ColorConfig {
    public MutableColor betonOfFloor = new MutableColor(new Color(3, 54, 73));
    public MutableColor floorWall = new MutableColor(new Color(74, 110, 94));

    public MutableColor elevatorFloorNumber = new MutableColor(new Color(59, 236, 0));
    public MutableColor elevatorDoor = new MutableColor(new Color(255, 172, 26));
    public MutableColor elevatorBorder = new MutableColor(new Color(0, 0, 0));
    public MutableColor elevatorBackGround = new MutableColor(new Color(193, 191, 255));
    public MutableColor elevatorButtonOn = new MutableColor(new Color(255, 0, 0));
    public MutableColor elevatorButtonOff = new MutableColor(new Color(0, 238, 61));


    public MutableColor[] customersSkin = {
            new MutableColor(new Color(255, 175, 175)),
            new MutableColor(new Color(96, 53, 53)),
            new MutableColor(new Color(245, 204, 204)),
            new MutableColor(new Color(208, 168, 130)),
            new MutableColor(new Color(232, 156, 123))
    };

    public MutableColor backGroundColor = new MutableColor(new Color(12, 12, 12));
    public MutableColor portalColor = new MutableColor(new Color(85, 67, 93));
    public MutableColor jButtonsColor = new MutableColor(new Color(30, 57, 98));
    public MutableColor menuButtonColor = new MutableColor(new Color(104, 218, 97, 102));
    public MutableColor blackSpacesColor = new MutableColor(new Color(101, 53, 141, 202));

}
