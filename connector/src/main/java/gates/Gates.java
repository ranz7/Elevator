package gates;

import controller.Tickable;
import dualConnectionStation.download.Downlink;
import dualConnectionStation.download.Reader;
import tools.Timer;
import dualConnectionStation.BaseDualConectionStation;
import protocol.Protocol;
import protocol.ProtocolMessage;
import protocol.ProtocolMessagesController;
import lombok.Setter;

import java.io.Serializable;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
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
    private final BaseDualConectionStation uplink;
    private final ProtocolMessagesController listener;
    private final LinkedList<ProtocolMessage> mesages = new LinkedList<>();

    // EVENTS
    @Setter
    Runnable onConnectEvent;

    @Setter
    Runnable onGatesCloseEvent;

    Runnable spamEvent;
    Timer spamTimer;

    // Filter Scenario
    @Setter
    ReceiveScenario receiveScenario = new ScenarioBuilder().build(ReciveFilters.noFilter());
    Map<Socket, Function<ProtocolMessage, Boolean>> sendFilters = new HashMap<>();

    Function<Protocol, Boolean> filter;

    public Gates(BaseDualConectionStation uplink, ProtocolMessagesController listener) {
        this.uplink = uplink;
        this.listener = listener;
        this.uplink.setDownlink(this);
    }

    public void connect() {
        Logger.getAnonymousLogger().info("Uplink start . . .");
        uplink.start();
    }

    @Override
    public void tick(double deltaTime) {
        if (uplink.isDisconnect()) {
            if (onGatesCloseEvent != null) {
                onGatesCloseEvent.run();
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
    public void onReceiveMessage(ProtocolMessage message) {
        if (isFiltered(message.getProtocol())) {
            return;
        }
        synchronized (mesages) {
            mesages.add(message);
        }
    }

    private boolean isFiltered(Protocol protocol) {
        filter = receiveScenario.get();
        if ((!filter.apply(protocol))) {
            receiveScenario.pop();
        }
        return filter.apply(protocol);
    }

    @Override
    public void onNewSocketConnection(Reader client) {
        Logger.getAnonymousLogger().info("Downlink connected . . .");
        if (onConnectEvent != null) {
            onConnectEvent.run();
        }
    }

    @Override
    public void onLostSocketConnection(Socket socket) {
        sendFilters.remove(socket);
        Logger.getAnonymousLogger().info("Downlink disconnected . . .");
    }

    private void send(ProtocolMessage message) throws NobodyReceivedMessageException {
        var ref = new Object() {
            Boolean wasSent = false;
        };
        uplink.getReceivers().forEach(socket -> {
            if (sendFilters.get(socket).apply(message)) {
                uplink.send(socket, message);
                ref.wasSent = true;
            }
        });
        if (!ref.wasSent) {
            throw new NobodyReceivedMessageException();
        }
    }

    public void send(Protocol protocol, int roomId, Serializable data) throws NobodyReceivedMessageException {
        send(new ProtocolMessage(protocol, roomId, data));
    }

    public void setSpamEvent(Runnable spamEvent, long timeToWaitBetweenSpams) {
        this.spamEvent = spamEvent;
        spamTimer = new Timer(timeToWaitBetweenSpams);
    }

    public void setSendFilter(Socket owner, Function<ProtocolMessage, Boolean> sendOnlyIfSubscribed) {
        sendFilters.put(owner, sendOnlyIfSubscribed);
    }

    public void sendWithoutCheck(Protocol protocol, int worldId, Serializable data) {
        try {
            send(protocol, worldId, data);
        } catch (NobodyReceivedMessageException e) {
            throw new RuntimeException("Nobody received message, please use usual send ");
        }
    }

    public static class NobodyReceivedMessageException extends Throwable {
    }
}