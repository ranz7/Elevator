package controller;

import connector.clientServer.Server;
import model.Model;

public class Main {
    public static void main(String[] args) {
        Controller controller = new Controller(new Model());
        Server server = new Server(controller);
        controller.setServer(server);
        controller.start();
    }
}
