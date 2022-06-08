package view.gui;

import lombok.Getter;
import settings.configs.WindowConfig;
import controller.Tickable;
import settings.localDraw.LocalDrawSetting;
import view.FPSAndPingcounter;
import view.gui.windowListeners.MouseListener;
import view.gui.windowListeners.ResizeListener;

import javax.swing.*;
import java.awt.*;

public class SwingWindow extends JPanel implements Tickable {
    private final JFrame jframe;
    @Getter
    private FPSAndPingcounter counter = new FPSAndPingcounter("42");
    private final Gui gui;
    private final MouseListener mouseListener;

    public SwingWindow(Gui gui, LocalDrawSetting localDrawSetting) {
        setLayout(null);
        setVisible(true);

        setSize(WindowConfig.WindowStartSize.width, WindowConfig.WindowStartSize.height);

        jframe = new JFrame("ELEVATOR SYS");
        jframe.setSize(WindowConfig.WindowStartSize.width, WindowConfig.WindowStartSize.height);
        jframe.setVisible(true);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.add(counter);
        jframe.add(this);

        setBackground(localDrawSetting.backGroundColor());
        mouseListener = new MouseListener(gui);
        addMouseListener(mouseListener);
        addResizeListener(new ResizeListener(gui));
        this.gui = gui;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        gui.draw(g);
    }

    @Override
    public void tick(double deltaTime) {
        counter.tick(deltaTime);
        mouseListener.tick(deltaTime);
        jframe.repaint();
    }

    public void addResizeListener(ResizeListener resizeListener) {
        this.addComponentListener(resizeListener);
    }
}