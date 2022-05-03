package controller;

import connector.clientServer.Client;
import model.GuiModel;

public class Main {
    public static void main(String[] args) {
        var windowModel = new GuiModel();
        var controller = new GuiController(windowModel);
        var client = new Client(controller);
        controller.setClient(client);
        if (args.length == 0) {
            client.setHost("localhost");
        }
        try {
            controller.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
