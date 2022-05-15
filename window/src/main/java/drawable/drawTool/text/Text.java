package drawable.drawTool.text;

import drawable.drawTool.DrawTool;
import lombok.Setter;
import tools.Vector2D;
import view.graphics.GameGraphics;

import java.awt.*;

public class Text extends DrawTool {
    @Setter
    private String text;
    @Setter
    private Color color;

    public Text(String text, Color color) {
        this.text = text;
        this.color = color;
    }

    @Override
    public void draw(Vector2D position, Vector2D size, GameGraphics drawer) {
        drawer.drawText(text,position, size.y, color);
        // TODO tutaj jak x jest 50 to text jest skrocony o 2 razy
    }
}
