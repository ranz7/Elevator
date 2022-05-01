
package view;


import controller.WindowController;
import model.WindowModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.Iterator;
import java.util.LinkedList;

public class Window {
    private ActionListener actionListener;
    private MouseListener mouseListener;

    private WindowController controller;

    private SwingPanel swingPanel;
    final Point WINDOW_SIZE = new Point(800, 800);

    private final LinkedList<JButton> ADD_CUSTOMER_BUTTONS = new LinkedList<>();
    private final LinkedList<JButton> ADD_REDUCE_ELEVATORS_BUTTONS = new LinkedList<>();
    private final LinkedList<JButton> CHANGE_SPEED_BUTTONS = new LinkedList<>();
    private final LinkedList<JButton> SELECT_FLOOR_BUTTONS = new LinkedList<>();

    private JFrame frame;
    private Dimension resize;

    public void startWindow(WindowModel windowModel, WindowController controller) {
        this.controller = controller;
        initializeWindow(windowModel);
        initializeButtons(windowModel);
    }

    private void initializeButtons(WindowModel windowModel) {
        actionListener = new GuiActionListener(this);
        mouseListener = new GuiMouseListener(this);
        for (int i = 0; i < 16; i++) {
            var addClientButton = createButton(
                    "->", windowModel.COLOR_SETTINGS.JBUTTONS_COLOR);
            addClientButton.setVisible(false);
            ADD_CUSTOMER_BUTTONS.add(addClientButton);

            var selectFloorButon = createButton(
                    String.valueOf(1),
                    windowModel.COLOR_SETTINGS.JBUTTONS_COLOR);
            selectFloorButon.setVisible(false);
            SELECT_FLOOR_BUTTONS.add(selectFloorButon);
        }
        ADD_REDUCE_ELEVATORS_BUTTONS.add(createButton("^", windowModel.COLOR_SETTINGS.JBUTTONS_COLOR));
        ADD_REDUCE_ELEVATORS_BUTTONS.add(createButton("v", windowModel.COLOR_SETTINGS.JBUTTONS_COLOR));
        CHANGE_SPEED_BUTTONS.add(createButton("<", windowModel.COLOR_SETTINGS.JBUTTONS_COLOR));
        CHANGE_SPEED_BUTTONS.add(createButton(">", windowModel.COLOR_SETTINGS.JBUTTONS_COLOR));
    }

    private JButton createButton(String text, Color buttonColor) {
        var startButton = new JButton(text);
        startButton.addActionListener(actionListener);
        startButton.addMouseListener(mouseListener);
        startButton.setBackground(buttonColor);
        startButton.setForeground(Color.white);
        startButton.setFocusPainted(false);
        swingPanel.add(startButton);
        return startButton;
    }


    private void initializeWindow(WindowModel windowModel) {
        swingPanel = new SwingPanel(windowModel);
        swingPanel.setBackground(windowModel.COLOR_SETTINGS.BLACK_SPACE_COLOR);
        swingPanel.setLayout(null);

        frame = new JFrame("ELEVATOR SYS");
        frame.setSize(WINDOW_SIZE.x, WINDOW_SIZE.y);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(swingPanel);
    }


    public void repaint() {
        frame.repaint();
    }

    public void updateButtons(WindowModel windowMODEL) {
        resize = swingPanel.getSize();
        Iterator<JButton> addCustomerButtonIt = ADD_CUSTOMER_BUTTONS.iterator();
        Iterator<JButton> selectFloorButtonIt = SELECT_FLOOR_BUTTONS.iterator();
        double heightOfButton = (resize.height - 100.) / windowMODEL.getSettings().FLOORS_COUNT;
        for (int i = 0; i < 16; i++) {
            JButton addCustomerButton = addCustomerButtonIt.next();
            addCustomerButton.setText("->");
            if (i >= windowMODEL.getSettings().FLOORS_COUNT) {
                addCustomerButton.setVisible(false);
                continue;
            }
            addCustomerButton.setVisible(true);
            addCustomerButton.setEnabled(true);
            addCustomerButton.setBounds(
                    new Rectangle(0, (int) heightOfButton * i + 50,
                            50, (int) heightOfButton));

            JButton selectFloorButton = selectFloorButtonIt.next();
            selectFloorButton.setVisible(true);
            selectFloorButton.setEnabled(true);
            selectFloorButton.setBounds(
                    new Rectangle( resize.width - 50, (int) heightOfButton * i + 50,
                            50, (int) heightOfButton));
        }

        for (int i = 0; i < 2; i++) {
            ADD_REDUCE_ELEVATORS_BUTTONS.get(i).setBounds(
                    new Rectangle(50, 50 + i * 100, 50, 50));
            CHANGE_SPEED_BUTTONS.get(i).setBounds(
                    new Rectangle(50 + i * 50, 100, 50, 50));
        }
    }

    public boolean resized() {
        if (resize == null) {
            return false;
        }
        return resize == swingPanel.getSize();
    }

    public void buttonClicked(JButton source) {
        if (ADD_REDUCE_ELEVATORS_BUTTONS.get(0) == source) {
            controller.changeElevatorsCount(true);
        }
        if (ADD_REDUCE_ELEVATORS_BUTTONS.get(1) == source) {
            controller.changeElevatorsCount(false);
        }
        if (CHANGE_SPEED_BUTTONS.get(0) == source) {
            controller.decreesSpeed();
        }
        if (CHANGE_SPEED_BUTTONS.get(1) == source) {
            controller.increaseSpeed();
        }

        Iterator<JButton> buttonIterator = ADD_CUSTOMER_BUTTONS.iterator();
        Iterator<JButton> newFloorButtonIterator = SELECT_FLOOR_BUTTONS.iterator();
        for (int i = 0; i < 16; i++) {
            JButton currentButton = buttonIterator.next();
            JButton newFloorButton = newFloorButtonIterator.next();
            if (newFloorButton == source) {
                var newFloorNumber = (Integer.parseInt(newFloorButton.getText())) % 6 + 1;

                newFloorButton.setText(String.valueOf(newFloorNumber));
                return;
            }
            if (currentButton == source) {
                controller.clickedAddCustomerButtonWithNumber(i, Integer.parseInt(newFloorButton.getText()));
                return;
            }
        }

    }

    public void rightMouseClicked(Point point) {
        if(swingPanel.zoomedIn()){
            swingPanel.zoomOut();
        }else{
            swingPanel.zoomIn(point,4.);
        }
    }
}

