package configs.tools;

import configs.RemoteConfig;
import configs.canvas.ColorConfig;
import configs.canvas.DrawConfig;
import lombok.Setter;
import tools.Vector2D;


public class CombienedDrawDataBase extends LocalDrawDataBase {
    @Setter
    private RemoteConfig remoteConfig;

    public CombienedDrawDataBase(ColorConfig colorConfig, DrawConfig drawConfig) {
        super(colorConfig, drawConfig);
    }

    public boolean initialized() {
        if (remoteConfig == null) {
            return false;
        }
        return super.initialized(remoteConfig.getClass());
    }

    public int floorsCount() {
        return remoteConfig.floorsCount;
    }


    public long elevatorOpenCloseTime() {
        return remoteConfig.elevatoropenclosetime;
    }


    public int elevatorsCount() {
        return remoteConfig.elevatorsCount;
    }


    public double floorHeight() {
        return buildingSize().y / floorsCount();
    }

    public Vector2D buildingSize() {
        return remoteConfig.buildingSize;
    }

    public double distanceBetweenElevators() {
        return buildingSize().x / (elevatorsCount() + 1);
    }


    public Vector2D customerSize() {
        return remoteConfig.customerSize;
    }

    public Vector2D elevatorSize() {
        return remoteConfig.elevatorSize;
    }

    public double buttonRelativePosition() {
        return remoteConfig.buttonRelativePosition;
    }

}
