package gates;

import controller.Tickable;
import dualConnectionStation.download.Downlink;
import dualConnectionStation.download.Reader;
import tools.Pair;
import tools.Timer;
import dualConnectionStation.BaseDualConectionStation;
import protocol.Protocol;
import protocol.ProtocolMessage;
import protocol.MessageApplier;
import lombok.Setter;

import java.io.Serializable;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
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
    private final MessageApplier listener;
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

    public Gates(BaseDualConectionStation uplink, MessageApplier listener) {
        this.uplink = uplink;
        this.listener = listener;
        this.uplink.setDownlink(this);
        Logger.getAnonymousLogger().info("Downlink created . . .");
    }

    public void connect() {
        Logger.getAnonymousLogger().info("Uplink starting . . .");
        uplink.start();
    }

    @Override
    public void tick(double deltaTime) {
        if (uplink.isDisconnected()) {
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
        uplink.flush();
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
        Logger.getAnonymousLogger().info("Uplink created . . .");
        if (onConnectEvent != null) {
            onConnectEvent.run();
        }
    }

    @Override
    public void onLostSocketConnection(Socket socket) {
        sendFilters.remove(socket);
        Logger.getAnonymousLogger().info("Uplink end . . .");
        if (onGatesCloseEvent != null) {
            onGatesCloseEvent.run();
        }
    }

    private void send(ProtocolMessage message) throws NobodyReceivedMessageException {
        var ref = new Object() {
            Boolean wasSent = false;
        };
        uplink.getReceivers().forEach(socket -> {
            if (!sendFilters.containsKey(socket) || sendFilters.get(socket).apply(message)) {
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

    public void sendWithoutCheckMultiple(Protocol protocol, List<Pair<Integer, Serializable>> dataList) {
        dataList.forEach(data -> {
            try {
                send(protocol, data.getFirst(), data.getSecond());
            } catch (NobodyReceivedMessageException e) {
                e.printStackTrace();
            }
        });
    }

    public static class NobodyReceivedMessageException extends Throwable {
    }
}
