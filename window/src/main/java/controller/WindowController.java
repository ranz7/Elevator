package controller;

import model.WindowModel;
import view.SwingWindow;

import java.util.concurrent.TimeUnit;

/*
 * control window, created with Swing
 *
 */
public class WindowController {
    private final WindowModel WINDOW_MODEL;
    private final SwingWindow GUI;

    public WindowController(WindowModel windowModel) {
        WINDOW_MODEL = windowModel;
        GUI = new SwingWindow();
    }


    public void start() throws InterruptedException {
        GUI.startWindow(WINDOW_MODEL, this);
        while (true) {
            if (!GUI.resized()) {
                GUI.updateButtonsAndSliders(WINDOW_MODEL);
            }
            GUI.repaint();
            TimeUnit.MILLISECONDS.sleep(Math.round(1000. / 50));
        }
    }
}
