package controller;

import model.WindowModel;
import view.SwingWindow;
import lombok.Setter;

import java.util.concurrent.TimeUnit;
import java.util.LinkedList;
import java.util.logging.Logger;

/*
 * control window, created with Swing
 * @see SwingWindow
 */
public class WindowController{
    static private final int TPS = 50;

    private final WindowModel WINDOW_MODEL;
    private final SwingWindow GUI;

    @Setter
    private long currentTime;
    private double gameSpeed = 1;

    public WindowController(WindowModel windowModel) {
        WINDOW_MODEL = windowModel;
        GUI = new SwingWindow();
    }

    public void start() throws InterruptedException {
        GUI.startWindow(WINDOW_MODEL, this);
        long lastTime = System.currentTimeMillis();

        while (true) {
            long deltaTime = System.currentTimeMillis() - lastTime;
            lastTime += deltaTime;
            currentTime += deltaTime;
                if (!GUI.resized()) {
                    GUI.updateButtons(WINDOW_MODEL);
                }
                WINDOW_MODEL.getDrawableOjects().forEach(object -> object.tick((long) (deltaTime * gameSpeed)));
            GUI.repaint();
            TimeUnit.MILLISECONDS.sleep(Math.round(1000. / TPS));
        }
    }
}
