package controller;

import model.Model;

import java.util.concurrent.TimeUnit;

public class Controller {
    public final Model MODEL;

    public Controller(Model model) {
        this.MODEL = model;
    }

    public void start() {
        while (true) {
            try {
                TimeUnit.MILLISECONDS.sleep(Math.round(1000. / 50));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
