package controller;

import model.WindowModel;

import java.util.concurrent.TimeUnit;

/*
 * control window, created with Swing
 *
 */
public class WindowController {
    private final WindowModel WINDOW_MODEL;
//  GUI;

    public WindowController(WindowModel windowModel) {
        WINDOW_MODEL = windowModel;
    }


    public void start() throws InterruptedException {
        while (true) {
//            GUI.repaint();
            TimeUnit.MILLISECONDS.sleep(Math.round(1000. / 50));
        }
    }
}
