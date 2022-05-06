package configs.CanvasSettings;

import configs.MainInitializationSettings;
import lombok.RequiredArgsConstructor;
import tools.Vector2D;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public class MainSettings {
    private final ColorSettings colorSettings;
    private final DrawSettings drawSettings;
    private MainInitializationSettings mainInitializationSettings;

    // TODO REFACTOR : create proxy
    public int floorsCount() {
        throwIfNull();
        return mainInitializationSettings.floorsCount;
    }

    private void throwIfNull() {
        List<Class> checkNotNull = Arrays.asList(
                mainInitializationSettings.getClass(), colorSettings.getClass(), drawSettings.getClass());
        checkNotNull.forEach(aClass -> {
            if (aClass == null) {
                throw new RuntimeException("Uncompleted settings:" + aClass.getName());
            }
        });
    }

    public Color elevatorBackGroundColor() {
        throwIfNull();
        return colorSettings.elevatorBackGround;
    }

    public long elevatorOpenCloseTime() {
        throwIfNull();
        return mainInitializationSettings.elevatoropenclosetime;
    }

    public Color doorsColor() {
        throwIfNull();
        return colorSettings.elevatorDoor;
    }

    public Color doorsBorder() {
        throwIfNull();
        return colorSettings.elevatorDoor;
    }

    public void set(MainInitializationSettings mainInitializationSettings) {
        throwIfNull();
        this.mainInitializationSettings = mainInitializationSettings;
    }

    public Color[] customerSkins() {
        throwIfNull();
        return colorSettings.customersSkin;
    }

    public boolean initialized() {
        var ref = new Object() {
            Boolean answer = true;
        };
        List<Class> checkNotNull = Arrays.asList(
                mainInitializationSettings.getClass(), colorSettings.getClass(), drawSettings.getClass());
        checkNotNull.forEach(aClass -> {
                    if (aClass == null) {
                        ref.answer = true;
                    }
                }
        );
        return ref.answer;
    }

    public int elevatorsCount() {
        throwIfNull();
        return mainInitializationSettings.elevatorsCount;
    }


    public double floorHeight() {
        throwIfNull();
        return buildingSize().y / floorsCount();
    }

    public Vector2D buildingSize() {
        return mainInitializationSettings.buildingSize;
    }

    public double distanceBetweenElevators() {
        throwIfNull();
        return buildingSize().x / (elevatorsCount() + 1);
    }

    public Color betonCollor() {
        throwIfNull();
        return colorSettings.betonOfFloor;
    }

    public Vector2D customerSize() {
        throwIfNull();
        return mainInitializationSettings.customerSize;
    }

    public Color windowBackGround() {
        return colorSettings.windowBackGround;
    }
}
