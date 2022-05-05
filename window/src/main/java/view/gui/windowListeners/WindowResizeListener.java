package view.gui.windowListeners;

import lombok.RequiredArgsConstructor;
import view.gui.Gui;
import view.gui.windowReacts.ResizeReact;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

@RequiredArgsConstructor
public class WindowResizeListener extends ComponentAdapter {
    final ResizeReact react;

    @Override
    public void componentResized(ComponentEvent e) {
        react.resize();
    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }
}
