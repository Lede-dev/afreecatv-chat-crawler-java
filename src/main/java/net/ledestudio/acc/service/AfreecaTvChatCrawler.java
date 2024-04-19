package net.ledestudio.acc.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.ledestudio.acc.client.AccClient;
import net.ledestudio.acc.client.AccClientHandler;
import net.ledestudio.acc.http.AccHttpRequestResult;
import net.ledestudio.acc.http.AccHttpRequester;
import net.ledestudio.acc.util.AccConstants;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.protocols.Protocol;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Set;
import java.util.concurrent.*;
import java.util.logging.Logger;

public class AfreecaTvChatCrawler {

    private static final Logger LOGGER = Logger.getLogger(AfreecaTvChatCrawler.class.getSimpleName());

    private AccClient client;
    private AccHttpRequestResult result;

    private ScheduledExecutorService scheduler;

    private final @NotNull String url;
    private final Set<AfreecaTvMessageReceiveEvent> events;
    private boolean autoReconnect;
    private long reconnectDelay; // ms

    public AfreecaTvChatCrawler(@NotNull String bid, int bno) {
        this(bid, Integer.toString(bno));
    }

    public AfreecaTvChatCrawler(@NotNull String bid, @NotNull String bno) {
        this(String.format("play.afreecatv.com/%s/%s", bid, bno));
    }

    public AfreecaTvChatCrawler(@NotNull String afreecaTvLiveUrl) {
        this(afreecaTvLiveUrl, false, 0);
    }

    public AfreecaTvChatCrawler(@NotNull String afreecaTvLiveUrl, boolean autoReconnect, long reconnectDelay) {
        this.url = afreecaTvLiveUrl;
        this.events = Sets.newHashSet();
        this.autoReconnect = autoReconnect;
        this.reconnectDelay = reconnectDelay;
    }

    public void registerMessageReceiveEvent(@NotNull AfreecaTvMessageReceiveEvent event) {
        events.add(event);
    }

    public void unregisterMessageReceiveEvent(@NotNull AfreecaTvMessageReceiveEvent event) {
        events.remove(event);
    }

    public Set<AfreecaTvMessageReceiveEvent> getEvents() {
        return events;
    }

    public void connect() {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                // Request AfreecaTv Live Information
                this.result = getLiveBroadcastResult();

                if (result == null) {
                    LOGGER.warning("Unable to find live broadcast information.");
                    return;
                }

                // Create Draft
                Draft_6455 draft = new Draft_6455(Lists.newArrayList(),
                        Lists.newArrayList(new Protocol("chat")));

                // Create WebSocket Client
                client = new AccClient(result, draft);

                // Create and Bind SSL Context
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, null, null);
                client.setSocketFactory(sslContext.getSocketFactory());

                // Create and Add Client Handler
                AccClientHandler handler = new AfreecaTvChatClientHandler(this);

                client.addHandler(handler);

                // Wait Connecting
                client.connectBlocking();

                // Create Scheduler
                if (client.isOpen()) {
                    sendPacket();
                }
            } catch (InterruptedException | ExecutionException
                     | NoSuchAlgorithmException | KeyManagementException e) {
                e.printStackTrace();
            }
        });
    }

    public void reconnect() {
        if (client != null) {
            try {
                this.result = getLiveBroadcastResult();
                if (result == null) {
                    LOGGER.warning("Unable to find live broadcast information.");
                    close();
                    return;
                }

                client.reconnectBlocking();
                sendPacket();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    public void close() {
        if (scheduler != null) {
            scheduler.shutdown();
        }
        if (client != null) {
            client.close();
        }
    }

    private @Nullable AccHttpRequestResult getLiveBroadcastResult() throws ExecutionException, InterruptedException {
        final AccHttpRequester requester = new AccHttpRequester(url);
        final CompletableFuture<AccHttpRequestResult> future = requester.request();
        return future.get();
    }

    private void sendPacket() {
        if (client == null) {
            return;
        }

        // Send Initial Packet
        client.send(AccConstants.createConnectPacket());
        client.send(AccConstants.createJoinPacket(result));

        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdownNow();
        }

        // Repeatedly Sending Ping Packet
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {
            client.send(AccConstants.createPingPacket());
        }, AccConstants.PING_PACKET_PERIOD_SECONDS, AccConstants.PING_PACKET_PERIOD_SECONDS, TimeUnit.SECONDS);
    }

    public @Nullable AccClient getClient() {
        return client;
    }

    public @Nullable AccHttpRequestResult getResult() {
        return result.clone();
    }

    public @NotNull String getUrl() {
        return url;
    }

    public boolean isAutoReconnect() {
        return autoReconnect;
    }

    public void setAutoReconnect(boolean autoReconnect) {
        this.autoReconnect = autoReconnect;
    }

    public long getReconnectDelay() {
        return reconnectDelay;
    }

    public void setReconnectDelay(long reconnectDelay) {
        this.reconnectDelay = reconnectDelay;
    }
}
