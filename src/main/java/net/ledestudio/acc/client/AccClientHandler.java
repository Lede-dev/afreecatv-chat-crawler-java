package net.ledestudio.acc.client;

import net.ledestudio.acc.http.AccHttpRequestResult;
import org.java_websocket.handshake.ServerHandshake;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;
import java.util.logging.Logger;

public abstract class AccClientHandler {

    private @Nullable AccClient client;
    private @Nullable AccHttpRequestResult result;
    private @Nullable Logger logger;

    public void init(@NotNull AccClient client, @NotNull AccHttpRequestResult result, @NotNull Logger logger) {
        this.client = client;
        this.result = result;
        this.logger = logger;
    }

    public @NotNull AccClient getClient() {
        if (client == null) {
            throw new NullPointerException("Accessing uninitialized fields in AccClientHandler without calling the init method.");
        }
        return client;
    }

    public @NotNull AccHttpRequestResult getResult() {
        if (result == null) {
            throw new NullPointerException("Accessing uninitialized fields in AccClientHandler without calling the init method.");
        }
        return result;
    }

    public @NotNull Logger getLogger() {
        if (logger == null) {
            throw new NullPointerException("Accessing uninitialized fields in AccClientHandler without calling the init method.");
        }
        return logger;
    }

    public abstract void onOpen(@NotNull ServerHandshake handshake);

    public abstract void onClose(int code, @NotNull String reason, boolean remote);

    public abstract void onMessage(@NotNull String message);

    public abstract void onMessage(@NotNull ByteBuffer bytes);

    public abstract void onMessageDecoded(@NotNull AccMessage message);

    public abstract void onError(@NotNull Exception ex);

}
