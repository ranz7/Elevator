package controller;

import connector.clientServer.Client;
import connector.clientServer.SocketCompactData;
import connector.clientServer.SocketEventListener;
import connector.protocol.ProtocolMessage;
import model.WindowModel;
import view.Window;
import lombok.Setter;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/*
 * control window, created with Swing
 * @see SwingWindow
 */
public class WindowController implements SocketEventListener {
    static private final int TPS = 50;

    private final WindowModel WINDOW_MODEL;
    private final Window GUI;

    @Setter
    private long currentTime;
    private double gameSpeed = 1;
    @Setter
    private Client client;

    private final Logger LOGGER = Logger.getLogger(WindowController.class.getName());

    public WindowController(WindowModel windowModel) {
        WINDOW_MODEL = windowModel;
        GUI = new Window();
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

    @Override
    public void onReceiveSocket(ProtocolMessage message) {
        LOGGER.info("RECIEVED MESSAGE");
    }

    @Override
    public void onNewSocketConnection(SocketCompactData message) {
        LOGGER.info("RECIEVED CONNECTION");
    }

    public void changeElevatorsCount(boolean isAdding) {
    }

    public void clickedAddCustomerButtonWithNumber(int startFloorButtonNumber, int endFloorNumber) {
        //send to main controller
    }

    public void increaseSpeed() {
        //send to main controller
    }

    public void decreesSpeed() {
        //send to main controller
    }
}
