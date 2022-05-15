package connector;

import tools.tools.Timer;
import connector.baseStation.BaseStation;
import connector.baseStation.download.SocketCompactData;
import connector.baseStation.download.Uplink;
import connector.filtersAndScenarios.FilterScenarios;
import connector.protocol.Protocol;
import connector.protocol.ProtocolMessage;
<<<<<<< Updated upstream
import connector.protocol.ProtocolMessageListener;
import connector.baseStation.Downlink;
=======
import connector.protocol.ProtocolMessagesController;
import connector.dualConnectionStation.upload.Uplink;
>>>>>>> Stashed changes
import lombok.Setter;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.function.Function;
import java.util.logging.Logger;

/**
 * Reads stream from Client or Server, as SocketEventListner
 * Controls Server or Client by Backhaul interface and send messages
 *
 * @see Downlink
 * @see Uplink
 */
public class Gates implements Uplink {


<<<<<<< Updated upstream
    private final BaseStation upload;
    private final ProtocolMessageListener listener;
=======
    private final BaseDualConectionStation upload;
    private final ProtocolMessagesController listener;
>>>>>>> Stashed changes
    private final LinkedList<ProtocolMessage> mesages = new LinkedList<>();

    // EVENTS
    @Setter
    Runnable onConnectEvent;

    @Setter
    Runnable onDisconnectEvent;

    Runnable spamEvent;
    Timer spamTimer;

    // Filter Scenario
    @Setter
    List<Function<Protocol, Boolean>> scenario = FilterScenarios.noFilter;
    Function<Protocol, Boolean> filter;

<<<<<<< Updated upstream
=======
    public Gates(BaseDualConectionStation upload, ProtocolMessagesController listener) {
        this.upload = upload;
        this.listener = listener;
        this.upload.setDownlink(this);
    }

>>>>>>> Stashed changes
    public void start() {
        Logger.getAnonymousLogger().info("Start Gates");
        upload.start();
    }

    public Gates(BaseStation upload, ProtocolMessageListener listener) {
        this.upload = upload;
        this.listener = listener;
        this.upload.setDownlink(this);
    }

    @Override
    public void onReceiveSocket(ProtocolMessage message) {

        if (isFiltered(message.getProtocolInMessage())) {
            return;
        }
        synchronized (mesages) {
            mesages.add(message);
        }
    }

    private boolean isFiltered(Protocol protocol) {
        filter = scenario.get(0);
        if ((!filter.apply(protocol)) && scenario.size() != 1) {
            scenario.remove(0);
        }
        return filter.apply(protocol);
    }

    @Override
    public void onNewSocketConnection(SocketCompactData client) {
        if (onConnectEvent != null) {
            onConnectEvent.run();
        }
    }

    public void tick(long deltaTime) {
        if (upload.isStopped()) {
            if (onDisconnectEvent != null) {
                onDisconnectEvent.run();
            }
            return;
        }

        if (spamTimer != null) {
            spamTimer.tick(deltaTime);
            if (spamTimer.isReady()) {
                spamEvent.run();
                spamTimer.restart();
            }
        }

        synchronized (mesages) {
            mesages.removeIf(listener::popMessage);
        }
    }

    public void send(ProtocolMessage message) {
        upload.send(message);
    }

    public void send(Protocol protocol, Serializable data) {
        send(new ProtocolMessage(protocol, data));
    }

    public void setSpamEvent(Runnable spamEvent, long timeToWaitBetweenSpams) {
        this.spamEvent = spamEvent;
        spamTimer = new Timer(timeToWaitBetweenSpams);
    }

}
