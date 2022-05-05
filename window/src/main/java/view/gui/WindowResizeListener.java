package view.gui;

import lombok.RequiredArgsConstructor;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

@RequiredArgsConstructor
public class WindowResizeListener extends ComponentAdapter {
    final Gui window;

    @Override
    public void componentResized(ComponentEvent e) {
        window.resize();
    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }
}
