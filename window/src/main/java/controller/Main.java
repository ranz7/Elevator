package controller;

import connector.clientServer.Client;
import model.WindowModel;

public class Main {
    public static void main(String[] args) {
        var windowModel = new WindowModel();
        var controller = new WindowController(windowModel);
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
