package view.buttons;

import model.WindowModel;
import view.canvas.GameCanvas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.LinkedList;

import view.window.Window;

public class ButtonsComponent {
    Dimension size;
    WindowModel windowModel;
    Window window;
    private ActionListener actionListener;

    private final LinkedList<JButton> ADD_CUSTOMER_BUTTONS = new LinkedList<>();
    private final LinkedList<JButton> ADD_REDUCE_ELEVATORS_BUTTONS = new LinkedList<>();
    private final LinkedList<JButton> CHANGE_SPEED_BUTTONS = new LinkedList<>();
    private final LinkedList<JButton> SELECT_FLOOR_BUTTONS = new LinkedList<>();

    public ButtonsComponent(Window window, WindowModel windowModel, GameCanvas buttonCarier) {
        this.size = window.getSize();
        this.window = window;
        this.windowModel = windowModel;
        actionListener = new GuiActionListener(this);
        for (int i = 0; i < 16; i++) {
            var addClientButton = createButton("->", windowModel.COLOR_SETTINGS.JBUTTONS_COLOR, buttonCarier);
            addClientButton.setVisible(false);
            ADD_CUSTOMER_BUTTONS.add(addClientButton);

            var selectFloorButon = createButton(String.valueOf(1), windowModel.COLOR_SETTINGS.JBUTTONS_COLOR, buttonCarier);
            selectFloorButon.setVisible(false);
            SELECT_FLOOR_BUTTONS.add(selectFloorButon);
        }
        ADD_REDUCE_ELEVATORS_BUTTONS.add(createButton("^", windowModel.COLOR_SETTINGS.JBUTTONS_COLOR, buttonCarier));
        ADD_REDUCE_ELEVATORS_BUTTONS.add(createButton("v", windowModel.COLOR_SETTINGS.JBUTTONS_COLOR, buttonCarier));
        CHANGE_SPEED_BUTTONS.add(createButton("<", windowModel.COLOR_SETTINGS.JBUTTONS_COLOR, buttonCarier));
        CHANGE_SPEED_BUTTONS.add(createButton(">", windowModel.COLOR_SETTINGS.JBUTTONS_COLOR, buttonCarier));
    }

    private JButton createButton(String text, Color buttonColor, GameCanvas buttonCarier) {
        var startButton = new JButton(text);
        startButton.addActionListener(actionListener);
        startButton.setBackground(buttonColor);
        startButton.setForeground(Color.white);
        startButton.setFocusPainted(false);
        buttonCarier.add(startButton);
        return startButton;
    }

    public void resize(Dimension size) {
        this.size = size;
        Iterator<JButton> addCustomerButtonIt = ADD_CUSTOMER_BUTTONS.iterator();
        Iterator<JButton> selectFloorButtonIt = SELECT_FLOOR_BUTTONS.iterator();
        double heightOfButton = (size.height - 100.) / windowModel.getSettings().FLOORS_COUNT;
        for (int i = 0; i < 16; i++) {
            JButton addCustomerButton = addCustomerButtonIt.next();
            addCustomerButton.setText("->");
            if (i >= windowModel.getSettings().FLOORS_COUNT) {
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
                    new Rectangle(size.width - 50, (int) heightOfButton * i + 50,
                            50, (int) heightOfButton));
        }

        for (int i = 0; i < 2; i++) {
            ADD_REDUCE_ELEVATORS_BUTTONS.get(i).setBounds(
                    new Rectangle(50, 50 + i * 100, 50, 50));
            CHANGE_SPEED_BUTTONS.get(i).setBounds(
                    new Rectangle(50 + i * 50, 100, 50, 50));
        }
    }

    public void buttonClicked(JButton source) {
        if (ADD_REDUCE_ELEVATORS_BUTTONS.get(0) == source) {
            window.addElevatorButtonClicked();
        }
        if (ADD_REDUCE_ELEVATORS_BUTTONS.get(1) == source) {
            window.removeElevatorButtonClicked();
        }

        if (CHANGE_SPEED_BUTTONS.get(0) == source) {
            window.decreaseGameSpeedButtonClicked();
        }
        if (CHANGE_SPEED_BUTTONS.get(1) == source) {
            window.increaseGameSpeedButtonClicked();
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
                window.clickedStartEndFloorButtonRequest(i, Integer.parseInt(newFloorButton.getText()));
                return;
            }
        }
    }

}
