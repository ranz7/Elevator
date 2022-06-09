package view.buttons;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.awt.*;

@AllArgsConstructor
public class MutableColor {
    @Getter
    @Setter
    private Color color;

    public MutableColor(int a, int b, int c) {
        color = new Color(a, b, c);
    }

    public int getRed() {
        return color.getRed();
    }

    public int getBlue() {
        return color.getBlue();
    }

    public int getGreen() {
        return color.getGreen();
    }
}
