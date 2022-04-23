package drawable;

import java.awt.*;

public interface Drawable {
    void draw(Graphics2D g2d);
    void tick(long deltaTime);
}
