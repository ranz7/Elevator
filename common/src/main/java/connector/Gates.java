package connector;

import architecture.tickable.Tickable;
import connector.filtersAndScenarios.Filters;
import connector.filtersAndScenarios.ScenarioBuilder;
import tools.Timer;
import connector.dualConnectionStation.BaseDualConectionStation;
import connector.dualConnectionStation.download.SocketCompactData;
import connector.dualConnectionStation.download.Downlink;
import connector.filtersAndScenarios.Scenario;
import connector.protocol.Protocol;
import connector.protocol.ProtocolMessage;
import connector.protocol.ProtocolMessagesConductor;
import connector.dualConnectionStation.upload.Uplink;
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
 * @see Uplink
 * @see Downlink
 */
public class Gates implements Tickable, Downlink {


    private final BaseDualConectionStation upload;
    private final ProtocolMessagesConductor listener;
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
    Scenario scenario = new ScenarioBuilder().build(Filters.noFilter);
    Function<Protocol, Boolean> filter;

    public Gates(BaseDualConectionStation upload, ProtocolMessagesConductor listener) {
        this.upload = upload;
        this.listener = listener;
        this.upload.setDownlink(this);
    }

    public void start() {
        Logger.getAnonymousLogger().info("Start Gates");
        upload.start();
    }

    @Override
    public void tick(double deltaTime) {
        if (upload.isDisconnect()) {
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
            mesages.removeIf(listener::applyMessage);
        }
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
        filter = scenario.get();
        if ((!filter.apply(protocol))) {
            scenario.pop();
        }
        return filter.apply(protocol);
    }

    @Override
    public void onNewSocketConnection(SocketCompactData client) {
        if (onConnectEvent != null) {
            onConnectEvent.run();
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
