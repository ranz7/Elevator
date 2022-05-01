package view;

import java.awt.event.ActionListener;
import javax.swing.*;


import java.awt.event.ActionEvent;

public record GuiActionListener(Window WINDOW) implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        WINDOW.buttonClicked((JButton) e.getSource());
        WINDOW.repaint();
    }
}
