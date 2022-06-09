package settings;

import configs.RoomPrepareCompactData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import settings.localDraw.LocalDrawSetting;
import tools.Vector2D;
import view.buttons.MutableColor;

import java.awt.*;
import java.util.Random;

@AllArgsConstructor
public class CombienedDrawSettings extends LocalDrawSetting {
    @Getter
    @Setter
    private RoomPrepareCompactData roomPrepareCompactData;

    public boolean initialized() {
        if (roomPrepareCompactData == null) {
            return false;
        }
        return super.initialized(roomPrepareCompactData.getClass());
    }

    public long elevatorOpenCloseTime() {
        return roomPrepareCompactData.elevatorOpenCloseTime();
    }

    public int roomId() {
        return roomPrepareCompactData.roomId();
    }

    public MutableColor getRandomCustomerSkin() {
        var colors = customerSkins();
        return colors[((Math.abs(new Random().nextInt())) % colors.length)];
    }

    public Vector2D customerSize() {
        return roomPrepareCompactData.customerSize();
    }

    public double gameSpeed() {
        return roomPrepareCompactData.gameSpeed();
    }
}
