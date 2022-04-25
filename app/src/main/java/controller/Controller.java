package controller;

import connector.clientServer.Server;
import connector.clientServer.SocketCompactData;
import connector.clientServer.SocketEventListener;
import connector.protocol.ProtocolMessage;
import lombok.Setter;
import model.Model;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Controller implements SocketEventListener {
    public final Model MODEL;
    private final Logger LOGGER = Logger.getLogger(Server.class.getName());

    private final int TPS = 50;
    @Setter
    private Server server;

    @Setter
    private double gameSpeed = 1;
    private long currentTime;

    public Controller(Model model) {
        this.MODEL = model;
    }

    public void start() {
        currentTime = System.currentTimeMillis();

        while (true) {
            long deltaTime = System.currentTimeMillis() - currentTime;
            currentTime += deltaTime;

            tickControllers((long) (deltaTime * gameSpeed));
            try {
                TimeUnit.MILLISECONDS.sleep(Math.round(1000. / TPS));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void tickControllers(long deltaTime) {
        // controllers tick tu
        MODEL.clearDead();
    }

    @Override
    public void onReceiveSocket(ProtocolMessage message) {
        LOGGER.info("RECIEVED MESSAGE");
    }

    @Override
    public void onNewSocketConnection(SocketCompactData message) {
        LOGGER.info("RECIEVED CONNECTION");
    }
}
