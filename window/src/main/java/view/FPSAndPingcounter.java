package view;

import controller.Tickable;
import settings.configs.GuiControllerConfig;

import java.awt.Color;
import java.util.List;
import javax.swing.*;
import java.util.LinkedList;

public class FPSAndPingcounter extends JLabel implements Tickable {
    private List<Double> values = new LinkedList();
    private final int countOfLastValues = 20;
    private final Color fpsColor = new Color(17, 255, 17);

    private int curPing = 0;

    @Override
    public void tick(double deltaTime) {
        Double newValue = 1000. / deltaTime;
        values.add(newValue);
        var average = -1;
        if (values.size() > countOfLastValues) {
            values.remove(0);
            average = (int) (values.stream()
                    .mapToDouble(a -> a)
                    .average()).getAsDouble();
            setText(average + " / " + GuiControllerConfig.TPS);
        }
    }

    long startTime = -1;

    public void ping() {
        if (startTime == -1) {
            startTime = System.currentTimeMillis();
            return;
        }
        curPing = (int) ((int) System.currentTimeMillis() - startTime);
        startTime = System.currentTimeMillis();
    }

    public FPSAndPingcounter(String s) {
        super(s);
        setSize(130, 80);
        setForeground(fpsColor);
    }
}
