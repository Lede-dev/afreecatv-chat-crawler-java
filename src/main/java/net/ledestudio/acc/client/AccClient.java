package net.ledestudio.acc.client;

import com.google.common.collect.Sets;
import net.ledestudio.acc.http.AccHttpRequestResult;
import net.ledestudio.acc.http.AccHttpResponseType;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.Set;
import java.util.logging.Logger;

public class AccClient extends WebSocketClient {

    private final Logger logger;
    private final AccHttpRequestResult result;

    private final Set<AccClientHandler> handlers = Sets.newHashSet();

    public AccClient(@NotNull AccHttpRequestResult result, @NotNull Draft protocolDraft) {
        super(result.toWebSocketURI(), protocolDraft);
        this.result = result;
        this.logger = Logger.getLogger(String.format("AccClient:%s", result.get(AccHttpResponseType.BID)));
    }

    public void addHandler(@NotNull AccClientHandler handler) {
        handler.init(this, result.clone(), logger);
        handlers.add(handler);
    }

    public void removeHandler(@NotNull AccClientHandler handler) {
        handlers.remove(handler);
    }

    public Set<AccClientHandler> getHandlers() {
        return Sets.newHashSet(handlers);
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
        handlers.forEach(handler -> handler.onOpen(handshake));
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        handlers.forEach(handler -> handler.onClose(code, reason, remote));
    }

    @Override
    public void onMessage(String message) {
        handlers.forEach(handler -> handler.onMessage(message));

        // decode message
        AccMessageDecoder decoder = new AccMessageDecoder(message);
        DecodedMessage decodedMessage = decoder.decode();
        handlers.forEach(handler -> handler.onMessageDecoded(decodedMessage));
    }

    @Override
    public void onMessage(ByteBuffer bytes) {
        handlers.forEach(handler -> handler.onMessage(bytes));
    }

    @Override
    public void onError(Exception ex) {
        handlers.forEach(handler -> handler.onError(ex));
    }

    public Logger getLogger() {
        return logger;
    }

    public AccHttpRequestResult getResult() {
        return result.clone();
    }
}
