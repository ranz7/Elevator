package connector;

import config.ConnectionSettings;
import connector.uplink.Server;
import connector.uplink.downlink.SocketCompactData;
import connector.uplink.downlink.SocketEventListener;
import connector.protocol.Protocol;
import connector.protocol.ProtocolMessage;
import connector.protocol.ProtocolMessageListener;
import lombok.Setter;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.function.Supplier;

public class Bridge implements SocketEventListener {
    @Setter
    Supplier<ProtocolMessage> onConnectionMessage;


    private final Server server;
    private final ProtocolMessageListener listener;
    private final LinkedList<ProtocolMessage> mesages = new LinkedList<>();

    public void start() {
        if (server != null) {
            server.start();
        }
    }

    public Bridge(Server server, ProtocolMessageListener listener) {
        this.server = server;
        this.listener = listener;
        this.server.setSocketEventListener(this);
    }

    @Override
    public void onReceiveSocket(ProtocolMessage message) {
        synchronized (mesages) {
            mesages.add(message);
        }
    }

    @Override
    public void onNewSocketConnection(SocketCompactData client) {
        if (onConnectionMessage != null) {
            server.Send(client, onConnectionMessage.get());
        }
    }

    public void tick(long deltaTime) {
        if (server == null) {
            return;
        }

        TIMER_TO_CHECK_SERVER.tick(deltaTime);
        if (TIMER_TO_CHECK_SERVER.isReady()) {
            server.Send(new ProtocolMessage(Protocol.UPDATE_DATA, model.getDataToSent()));
            TIMER_TO_CHECK_SERVER.restart(Math.round(1000. / ConnectionSettings.SSPS));
        }

        synchronized (mesages) {
            mesages.removeIf(protocolMessage ->
                    listener.popMessage(
                            protocolMessage.getProtocolInMessage(),
                            protocolMessage.getDataInMessage())
            );
        }
    }

    public void Send(Protocol protocol, Serializable data) {
        server.Send(new ProtocolMessage(protocol, data));
    }
}
