package view;

import java.awt.event.ActionListener;
import javax.swing.*;


import java.awt.event.ActionEvent;

public record GuiActionListener(SwingWindow WINDOW) implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        WINDOW.clicked((JButton) e.getSource());
        WINDOW.repaint();
    }
}
