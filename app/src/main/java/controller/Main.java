package controller;

import model.Model;

public class Main {
    public static void main(String[] args) {
        AppController appController = new AppController();
        appController.setModel(new Model());
        appController.start();
    }
}
