package drawable;

import lombok.RequiredArgsConstructor;

import java.awt.*;

@RequiredArgsConstructor
public class RainbowColor {
    private final Color startColor;
    private Color lastColor ;

    boolean increaseRed = true;
    boolean increaseGreen = true;
    boolean increaseBlue = true;
    int r = 2;
    int g = 2;
    int b = 2;

    public Color next() {
        if(lastColor==null){
            lastColor =startColor;
        }
        int red = lastColor.getRed();
        if (increaseRed) {
            if (red + r >= 255-r) {
                increaseRed = false;
            }
            red += r;
        } else {
            if (red - r <=  r ) {
                increaseRed = true;
            }
            red -= r;
        }
        int green = lastColor.getGreen();
        if (increaseGreen) {
            if (green + g >= 255 - g ) {
                increaseGreen = false;
            }
            green += g;
        } else {
            if (green - g <= g) {
                increaseGreen = true;
            }
            green -= g;
        }
        int blue = lastColor.getBlue();
        if (increaseBlue) {
            if (blue + b >= 255 - b) {
                increaseBlue = false;
            }
            blue += b;
        } else {
            if (blue - b <= b) {
                increaseBlue = true;
            }
            blue -= b;
        }
        lastColor = new Color(
                red,
                green,
                blue
        );
        return lastColor;
    }
}
