package view.Window;

import lombok.RequiredArgsConstructor;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

@RequiredArgsConstructor
public class GuiResizeListener extends ComponentAdapter {
    final Window window;

    @Override
    public void componentResized(ComponentEvent e) {
        window.resize();
    }

    @Override
    public void componentMoved(ComponentEvent e) {
        System.out.println("Moved");
    }
}
