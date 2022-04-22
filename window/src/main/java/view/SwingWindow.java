
package view;

import javax.swing.*;
import java.awt.*;
public class SwingWindow {
    private SwingPanel startPanel;
    final Point WINDOW_SIZE = new Point(800, 800);

    private JFrame frame;

    public void startWindow() {
        initializeWindow();
    }

    private void initializeWindow() {
        startPanel = new SwingPanel();
        startPanel.setBackground(new Color(0, 0, 0));
        startPanel.setLayout(null);

        frame = new JFrame("ELEVATOR SYS");
        frame.setSize(WINDOW_SIZE.x, WINDOW_SIZE.y);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(startPanel);
    }


    public void repaint() {
        frame.repaint();
    }

}

