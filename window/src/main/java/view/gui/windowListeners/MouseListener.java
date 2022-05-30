package view.gui.windowListeners;

import tools.Vector2D;
import view.gui.windowReacts.MouseReact;

import javax.swing.*;
import java.awt.event.MouseEvent;

public record MouseListener(MouseReact react) implements java.awt.event.MouseListener {

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            react.rightMouseClicked(new Vector2D(e.getPoint()));
        }
        if(SwingUtilities.isLeftMouseButton(e)){
            react.leftMouseClicked(new Vector2D(e.getPoint()));
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
