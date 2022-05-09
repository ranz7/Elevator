package configs.tools;

import configs.ConnectionEstalblishConfig;
import configs.canvas.ColorConfig;
import configs.canvas.DrawConfig;
import lombok.Setter;
import tools.Vector2D;

import java.awt.*;
import java.util.Random;


public class CombienedDrawDataBase extends LocalDrawDataBase {
    @Setter
    private ConnectionEstalblishConfig connectionEstalblishConfig;

    public CombienedDrawDataBase(ColorConfig colorConfig, DrawConfig drawConfig) {
        super(colorConfig, drawConfig);
    }

    public boolean initialized() {
        if (connectionEstalblishConfig == null) {
            return false;
        }
        return super.initialized(connectionEstalblishConfig.getClass());
    }

    public int floorsCount() {
        return connectionEstalblishConfig.floorsCount;
    }


    public long elevatorOpenCloseTime() {
        return connectionEstalblishConfig.elevatoropenclosetime;
    }


    public int elevatorsCount() {
        return connectionEstalblishConfig.elevatorsCount;
    }


    public double floorHeight() {
        return buildingSize().y / floorsCount();
    }

    public Vector2D buildingSize() {
        return connectionEstalblishConfig.buildingSize;
    }

    public Vector2D customerSize() {
        return connectionEstalblishConfig.customerSize;
    }

    public Vector2D elevatorSize() {
        return connectionEstalblishConfig.elevatorSize;
    }

    public double buttonRelativePosition() {
        return connectionEstalblishConfig.buttonRelativePosition;
    }

    public double distanceBetweenElevators() {
        return buildingSize().x / (elevatorsCount() + 1);
    }

    public Color getRandomCustomerSkin() {
        var colors =  customerSkins();
        return colors[(int) ((Math.abs( new Random().nextInt())) % colors.length)];
    }
}
