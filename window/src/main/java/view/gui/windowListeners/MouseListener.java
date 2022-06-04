package view.gui.windowListeners;

import controller.Tickable;
import lombok.RequiredArgsConstructor;
import tools.Pair;
import tools.Vector2D;
import view.gui.windowReacts.MouseReact;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;

@RequiredArgsConstructor
public class MouseListener implements java.awt.event.MouseListener, Tickable {
    private final MouseReact react;
    private List<Pair<MouseEvent, MouseEventType>> events = new LinkedList<>();

    enum MouseEventType {
        MOUSE_PRESSED,
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        synchronized (events) {
            events.add(new Pair<>(e, MouseEventType.MOUSE_PRESSED));
        }
    }

    @Override
    public void tick(double deltaTime) {
        events.forEach(event -> {
                    switch (event.getSecond()) {
                        case MOUSE_PRESSED -> {
                            if (SwingUtilities.isRightMouseButton(event.getFirst())) {
                                react.rightMouseClicked(new Vector2D(event.getFirst().getPoint()));
                            }
                            if (SwingUtilities.isLeftMouseButton(event.getFirst())) {
                                react.leftMouseClicked(new Vector2D(event.getFirst().getPoint()));
                            }
                        }
                    }
                }
        );
        events.clear();
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
