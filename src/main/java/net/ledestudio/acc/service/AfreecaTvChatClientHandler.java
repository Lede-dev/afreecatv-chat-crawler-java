package net.ledestudio.acc.service;

import net.ledestudio.acc.client.AccClientHandler;
import net.ledestudio.acc.client.AccMessage;
import org.java_websocket.handshake.ServerHandshake;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public class AfreecaTvChatClientHandler extends AccClientHandler {

    @Override
    public void onOpen(@NotNull ServerHandshake handshake) {
        getLogger().info("Open connection");
    }

    @Override
    public void onClose(int code, @NotNull String reason, boolean remote) {
        getLogger().info("close connection");
    }

    @Override
    public void onMessage(@NotNull String message) {
        getLogger().info("receive message : " + message);
    }

    @Override
    public void onMessage(@NotNull ByteBuffer bytes) {
        getLogger().info("receive byte message : " + bytes);
    }

    @Override
    public void onMessageDecoded(@NotNull AccMessage message) {
        getLogger().info("decode message : " + message);
    }

    @Override
    public void onError(@NotNull Exception ex) {
        ex.printStackTrace();
    }

}
