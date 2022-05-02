package view.canvas;

import tools.Vector2D;
import view.window.Window;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public record GuiMouseListener(Window WINDOW) implements MouseListener {

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            WINDOW.rightMouseClicked(new Vector2D(e.getPoint()));
        }
        if(SwingUtilities.isLeftMouseButton(e)){
            WINDOW.leftMouseClicked(new Vector2D(e.getPoint()));
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}