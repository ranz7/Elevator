package view.buttons;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import settings.localDraw.LocalDrawSetting;
import view.gui.windowListeners.ButtonsListener;
import view.gui.windowReacts.ButtonsReact;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

@RequiredArgsConstructor
public class ButtonsComponent {
    @Setter
    private ActionListener listenWindow;

    private final List<JButton> ADD_CUSTOMER_BUTTONS = new ArrayList<>();
    private final List<JButton> SELECT_FLOOR_BUTTONS = new ArrayList<>();

    private JButton createButton(String text, Color buttonColor, JComponent buttonCarier) {
        var startButton = new JButton(text);
        startButton.addActionListener(listenWindow);
        startButton.setBackground(buttonColor);
        startButton.setForeground(Color.white);
        startButton.setFocusPainted(false);
        buttonCarier.add(startButton);
        return startButton;
    }

    public void resize(Dimension size) {/*
        Iterator<JButton> addCustomerButtonIt = ADD_CUSTOMER_BUTTONS.iterator();
        Iterator<JButton> selectFloorButtonIt = SELECT_FLOOR_BUTTONS.iterator();
        double heightOfButton = (size.height - 100.) / settings.floorsCount();
        for (int i = 0; i < 16; i++) {
            JButton addCustomerButton = addCustomerButtonIt.next();
            addCustomerButton.setText("->");
            if (i >= settings.floorsCount()) {
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
        }*/
    }

    public void buttonClicked(JButton source) {
//        if (ADD_REDUCE_ELEVATORS_BUTTONS.get(0) == source) {
//            react.addElevatorButtonClicked();
//        }
//        if (ADD_REDUCE_ELEVATORS_BUTTONS.get(1) == source) {
//            react.removeElevatorButtonClicked();
//        }
//
//        if (CHANGE_SPEED_BUTTONS.get(0) == source) {
//            react.decreaseGameSpeedButtonClicked();
//        }
//        if (CHANGE_SPEED_BUTTONS.get(1) == source) {
//            react.increaseGameSpeedButtonClicked();
//        }

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
         //       react.clickedStartEndFloorButtonRequest(i, Integer.parseInt(newFloorButton.getText()));
                return;
            }
        }
    }

}
