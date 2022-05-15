package view.buttons;

<<<<<<< Updated upstream
=======
import settings.CombienedDrawSettings;
import lombok.RequiredArgsConstructor;
>>>>>>> Stashed changes
import lombok.Setter;
import model.GuiModel;
import view.gui.ButtonsListener;
import view.canvas.GameWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

import view.gui.Gui;

public class ButtonsComponent {
<<<<<<< Updated upstream
    private Gui gui;
    private GuiModel guiModel;
    private final GameWindow gameWindow;
=======
    private final JComponent window;
    private CombienedDrawSettings settings;
>>>>>>> Stashed changes
    @Setter
    private ActionListener buttonListener;

    private final LinkedList<JButton> ADD_CUSTOMER_BUTTONS = new LinkedList<>();
    private final LinkedList<JButton> ADD_REDUCE_ELEVATORS_BUTTONS = new LinkedList<>();
    private final LinkedList<JButton> CHANGE_SPEED_BUTTONS = new LinkedList<>();
    private final LinkedList<JButton> SELECT_FLOOR_BUTTONS = new LinkedList<>();

    public ButtonsComponent(Gui gui, GameWindow gameWindow) {
        this.gui = gui;
        this.gameWindow = gameWindow;
    }

    public void start() {
        for (int i = 0; i < 16; i++) {
            var addClientButton = createButton("->", guiModel.COLOR_SETTINGS.JBUTTONS_COLOR, gameWindow);
            addClientButton.setVisible(false);
            ADD_CUSTOMER_BUTTONS.add(addClientButton);

            var selectFloorButon = createButton(String.valueOf(1), guiModel.COLOR_SETTINGS.JBUTTONS_COLOR, gameWindow);
            selectFloorButon.setVisible(false);
            SELECT_FLOOR_BUTTONS.add(selectFloorButon);
        }
        ADD_REDUCE_ELEVATORS_BUTTONS.add(createButton("^", guiModel.COLOR_SETTINGS.JBUTTONS_COLOR, gameWindow));
        ADD_REDUCE_ELEVATORS_BUTTONS.add(createButton("v", guiModel.COLOR_SETTINGS.JBUTTONS_COLOR, gameWindow));
        CHANGE_SPEED_BUTTONS.add(createButton("<", guiModel.COLOR_SETTINGS.JBUTTONS_COLOR, gameWindow));
        CHANGE_SPEED_BUTTONS.add(createButton(">", guiModel.COLOR_SETTINGS.JBUTTONS_COLOR, gameWindow));
    }

    private JButton createButton(String text, Color buttonColor, GameWindow buttonCarier) {
        var startButton = new JButton(text);
        startButton.addActionListener(buttonListener);
        startButton.setBackground(buttonColor);
        startButton.setForeground(Color.white);
        startButton.setFocusPainted(false);
        buttonCarier.add(startButton);
        return startButton;
    }

    public void resize(Dimension size) {
        if (ADD_REDUCE_ELEVATORS_BUTTONS.isEmpty()) {
            return;
        }
        Iterator<JButton> addCustomerButtonIt = ADD_CUSTOMER_BUTTONS.iterator();
        Iterator<JButton> selectFloorButtonIt = SELECT_FLOOR_BUTTONS.iterator();
        double heightOfButton = (size.height - 100.) / guiModel.getSettings().FLOORS_COUNT;
        for (int i = 0; i < 16; i++) {

            JButton addCustomerButton = addCustomerButtonIt.next();
            addCustomerButton.setText("->");
            if (i >= guiModel.getSettings().FLOORS_COUNT) {
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
            gui.addElevatorButtonClicked();
        }
        if (ADD_REDUCE_ELEVATORS_BUTTONS.get(1) == source) {
            gui.removeElevatorButtonClicked();
        }

        if (CHANGE_SPEED_BUTTONS.get(0) == source) {
            gui.decreaseGameSpeedButtonClicked();
        }
        if (CHANGE_SPEED_BUTTONS.get(1) == source) {
            gui.increaseGameSpeedButtonClicked();
        }

        List<Integer> a = new ArrayList<>();
        Set<String> b = new HashSet<>();
        HashSet<String> c = new HashSet<>();
        c.equals(b);

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
                gui.clickedStartEndFloorButtonRequest(i, Integer.parseInt(newFloorButton.getText()));
                return;
            }
        }
    }

    public void addButtonsListener(Gui gui, ButtonsListener buttonsListener) {
        this.buttonListener = buttonsListener;
        this.gui = gui;
    }

    public void setModel(GuiModel model) {
<<<<<<< Updated upstream
        this.guiModel = model;
=======
        settings = model.getCombienedDrawSettings();
>>>>>>> Stashed changes
    }
}
