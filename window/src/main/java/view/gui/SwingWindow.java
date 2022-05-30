package view.gui;

import settings.configs.WindowConfig;
import controller.Tickable;
import settings.localDraw.LocalDrawSetting;
import view.FPScounter;
import view.gui.windowListeners.MouseListener;
import view.gui.windowListeners.ResizeListener;

import javax.swing.*;
import java.awt.*;

public class SwingWindow extends JPanel implements Tickable {
    private final JFrame jframe;
    private FPScounter counter = new FPScounter("42");
    private final Gui gui;

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

        addMouseListener(new MouseListener(gui));
        addResizeListener(new ResizeListener(gui));
        this.gui = gui;
    }

    public void start() {
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        gui.draw(g);
    }

    @Override
    public void tick(double deltaTime) {
        counter.tick(deltaTime);
        jframe.repaint();
    }

    public void addResizeListener(ResizeListener resizeListener) {
        this.addComponentListener(resizeListener);
    }
}