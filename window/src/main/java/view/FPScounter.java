package view;

import controller.Tickable;
import settings.configs.GuiControllerConfig;

import java.awt.Color;
import java.util.List;
import javax.swing.*;
import java.util.LinkedList;

public class FPScounter extends JLabel implements Tickable {
    private List<Double> values = new LinkedList();
    private final int countOfLastValues = 20;
    private final Color fpsColor = new Color(17, 255, 17);

    @Override
    public void tick(double deltaTime) {
        Double newValue = 1000. / deltaTime;
        values.add(newValue);
        if (values.size() > countOfLastValues) {
            values.remove(0);
            var average = (int) (values.stream()
                    .mapToDouble(a -> a)
                    .average()).getAsDouble();
            setText(average + " / " + GuiControllerConfig.TPS);
        }
    }

    public FPScounter(String s) {
        super(s);
        setSize(130, 40);
        setForeground(fpsColor);
    }
}
