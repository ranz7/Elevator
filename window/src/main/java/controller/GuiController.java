package controller;

import connector.clientServer.Client;
import connector.clientServer.SocketCompactData;
import connector.clientServer.SocketEventListener;
import connector.protocol.ProtocolMessage;
import model.GuiModel;
import tools.Vector2D;
import view.gui.Gui;
import lombok.Setter;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/*
 * control window, created with Swing
 * @see SwingWindow
 */
public class GuiController implements SocketEventListener {
    static private final int TPS = 50;

    private final GuiModel WINDOW_MODEL;
    private final Gui GUI;

    @Setter
    private long currentTime;
    private double gameSpeed = 1;
    @Setter
    private Client client;

    private final Logger LOGGER = Logger.getLogger(GuiController.class.getName());

    public GuiController(GuiModel guiModel) {
        WINDOW_MODEL = guiModel;
        GUI = new Gui(WINDOW_MODEL,this);
    }

    public void start() throws InterruptedException {
        GUI.start();
        long lastTime = System.currentTimeMillis();

        while (true) {
            long deltaTime = System.currentTimeMillis() - lastTime;
            lastTime += deltaTime;
            currentTime += deltaTime;
            WINDOW_MODEL.getDrawableOjects().forEach(object -> object.tick((long) (deltaTime * gameSpeed)));
            GUI.update();
            TimeUnit.MILLISECONDS.sleep(Math.round(1000. / TPS));
            WINDOW_MODEL.clearDead();
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

    public void decreaseGameSpeed() {
        //send to main controller
    }

    public void clickButton(Vector2D point) {
        var pointInGame = GUI.getGameWindow().getGAME_SCALER().getFromRealToGameCoordinate(point, 0);
        var button = WINDOW_MODEL.getNearestButton(pointInGame);
        if (button.getPosition().distanceTo(pointInGame) <20) {
            button.buttonClick();

        }
    }
}
