
package view;

import controller.WindowController;
import model.WindowModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.LinkedList;

public class SwingWindow {
    private SwingPanel startPanel;
    final Point WINDOW_SIZE = new Point(800, 800);
    private WindowController controller;
    private ActionListener listener;

    private final LinkedList<JButton> ADD_CLIENT_BUTTONS = new LinkedList<>();
    private JFrame frame;
    private Dimension resize;

    public void startWindow(WindowModel windowModel, WindowController controller) {
        this.controller = controller;
        initializeWindow(windowModel);
        initializeButtons(windowModel);
    }

    private void initializeButtons(WindowModel windowModel) {
        listener = new GuiActionListener(this);
        for (int i = 0; i < 16; i++) {
            var buttonCreated = createButton(
                    "->", windowModel.COLOR_SETTINGS.JBUTTONS_COLOR);
            buttonCreated.setVisible(false);
            ADD_CLIENT_BUTTONS.add(buttonCreated);
            var floorButton = createButton(
                    String.valueOf(1),
                    windowModel.COLOR_SETTINGS.JBUTTONS_COLOR);
            floorButton.setVisible(false);
        }
    }

    private JButton createButton(String text, Color buttonColor) {
        var startButton = new JButton(text);
        startButton.addActionListener(listener);
        startButton.setBackground(buttonColor);
        startButton.setForeground(Color.white);
        startButton.setFocusPainted(false);
        startPanel.add(startButton);
        return startButton;
    }

    private void initializeWindow(WindowModel windowModel) {
        startPanel = new SwingPanel(windowModel);
        startPanel.setBackground(windowModel.COLOR_SETTINGS.BLACK_SPACE_COLOR);
        startPanel.setLayout(null);

        frame = new JFrame("ELEVATOR SYS");
        frame.setSize(WINDOW_SIZE.x, WINDOW_SIZE.y);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(startPanel);
    }


    public boolean resized() {
        if (resize == null) {
            return false;
        }
        return resize == startPanel.getSize();
    }

    public void updateButtonsAndSliders(WindowModel windowMODEL) {
        resize = startPanel.getSize();
        Iterator<JButton> button = ADD_CLIENT_BUTTONS.iterator();
        int num_of_floors =  3;
        double heightOfButton = (resize.height - 100.) /num_of_floors;
        for (int i = 0; i < 16; i++) {
            JButton currentButton = button.next();
            currentButton.setText("->");
            if (i >= num_of_floors) {
                currentButton.setVisible(false);
                continue;
            }
            currentButton.setVisible(true);
            currentButton.setEnabled(true);
            currentButton.setBounds(
                    new Rectangle(0, (int) heightOfButton * i + 50,
                            50, (int) heightOfButton));
        }

    }


    public void repaint() {
        frame.repaint();
    }


    public void clicked(JButton source) {

        Iterator<JButton> buttonIterator = ADD_CLIENT_BUTTONS.iterator();
        for (int i = 0; i < 16; i++) {
            JButton currentButton = buttonIterator.next();

            if (currentButton == source) {
                System.out.println("CLICKED" + i);
                return;
            }
        }

    }
}

