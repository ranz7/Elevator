package view.Canvas;

import view.Window.Window;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public record GuiMouseListener(Window WINDOW) implements MouseListener {

    @Override
    public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            WINDOW.rightMouseClicked(e.getPoint());
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
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
