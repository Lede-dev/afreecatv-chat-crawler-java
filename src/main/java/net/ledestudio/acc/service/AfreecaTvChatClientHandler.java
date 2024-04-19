package net.ledestudio.acc.service;

import net.ledestudio.acc.client.AccClientHandler;
import net.ledestudio.acc.client.AccMessage;
import org.java_websocket.handshake.ServerHandshake;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AfreecaTvChatClientHandler extends AccClientHandler {

    private final @NotNull AfreecaTvChatCrawler crawler;

    public AfreecaTvChatClientHandler(@NotNull AfreecaTvChatCrawler crawler) {
        this.crawler = crawler;
    }

    @Override
    public void onOpen(@NotNull ServerHandshake handshake) {
        getLogger().info("WebSocket connection established in the AfreecaTvChatClientHandler.");
    }

    @Override
    public void onClose(int code, @NotNull String reason, boolean remote) {
        getLogger().info("WebSocket connection closed in the AfreecaTvChatClientHandler.");
        crawler.close();

        if (crawler.isAutoReconnect()) {
            Executors.newSingleThreadScheduledExecutor().schedule(() -> {
                getLogger().info("Reconnecting WebSocket in AfreecaTvChatClientHandler.");
                crawler.reconnect();
            }, crawler.getReconnectDelay(), TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public void onMessage(@NotNull String message) {}

    @Override
    public void onMessage(@NotNull ByteBuffer bytes) {}

    @Override
    public void onMessageDecoded(@NotNull AccMessage message) {
        crawler.getEvents().forEach(event -> event.onMessageReceive(message));
    }

    @Override
    public void onError(@NotNull Exception ex) {
        ex.printStackTrace();
    }

    public @NotNull AfreecaTvChatCrawler getCrawler() {
        return crawler;
    }

}
