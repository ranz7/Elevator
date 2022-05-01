package view.buttons;

import java.awt.event.ActionListener;
import javax.swing.*;


import java.awt.event.ActionEvent;

public record GuiActionListener(ButtonsComponent buttonsComponent) implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        buttonsComponent.buttonClicked((JButton) e.getSource());
    }
}
