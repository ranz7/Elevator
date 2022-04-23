package model;

import drawable.ColorSettings;
import drawable.Drawable;
import drawable.drawableObjects.*;
import lombok.Getter;
import lombok.Setter;
import java.util.LinkedList;


public class WindowModel {
    public final ColorSettings COLOR_SETTINGS = new ColorSettings();

    @Getter
    @Setter
    private long lastServerRespondTime = 0;

    private LinkedList<DrawableCustomer> customers = new LinkedList<>();

    public LinkedList<Drawable> getDrawableOjects() {
        LinkedList<Drawable> drawables = new LinkedList<>();
        drawables.addAll(customers);
        return drawables;
    }
}
