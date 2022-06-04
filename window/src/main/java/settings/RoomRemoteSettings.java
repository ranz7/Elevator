package settings;

import configs.RoomPrepareCompactData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import settings.localDraw.LocalDrawSetting;
import tools.Vector2D;

@AllArgsConstructor
public class RoomRemoteSettings  {
    @Getter
    private final RoomPrepareCompactData roomPrepareCompactData;

    public int roomId() {
        return roomPrepareCompactData.roomId();
    }
    public double gameSpeed() {
        return roomPrepareCompactData.gameSpeed();
    }

}
