package connector;

import common.tools.Timer;
import connector.baseStation.BaseStation;
import connector.baseStation.download.SocketCompactData;
import connector.baseStation.download.Uplink;
import connector.filtersAndScenarios.FilterScenarios;
import connector.protocol.Protocol;
import connector.protocol.ProtocolMessage;
import connector.protocol.ProtocolMessageListener;
import connector.baseStation.Downlink;
import lombok.Setter;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
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


    private final BaseStation upload;
    private final ProtocolMessageListener listener;
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
        System.out.println(message.getProtocolInMessage());

        if (isFiltered(message.getProtocolInMessage())) {
            return;
        }
        System.out.println(message.getProtocolInMessage());
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
            System.out.println("SPAM timer");
            spamTimer.tick(deltaTime);
            if (spamTimer.isReady()) {
                System.out.println("DO SPAM");
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
