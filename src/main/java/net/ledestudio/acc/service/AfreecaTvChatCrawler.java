package net.ledestudio.acc.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.ledestudio.acc.client.AccClient;
import net.ledestudio.acc.client.AccClientHandler;
import net.ledestudio.acc.http.AccHttpRequestResult;
import net.ledestudio.acc.http.AccHttpRequester;
import net.ledestudio.acc.util.AccConstants;
import org.apache.http.ssl.SSLContextBuilder;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.enums.ReadyState;
import org.java_websocket.protocols.IProtocol;
import org.java_websocket.protocols.Protocol;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

public class AfreecaTvChatCrawler {

    private AccClient client;
    private AccHttpRequestResult result;

    private final @NotNull String url;

    public AfreecaTvChatCrawler(@NotNull String bid, int bno) {
        this(bid, Integer.toString(bno));
    }

    public AfreecaTvChatCrawler(@NotNull String bid, @NotNull String bno) {
        this(String.format("play.afreecatv.com/%s/%s", bid, bno));
    }

    public AfreecaTvChatCrawler(@NotNull String afreecaTvLiveUrl) {
        this.url = afreecaTvLiveUrl;
    }

    public void connect() {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                // Request AfreecaTv Live Information
                final AccHttpRequester requester = new AccHttpRequester(url);
                final CompletableFuture<AccHttpRequestResult> future = requester.request();
                this.result = future.get();

                System.out.println(result.toWebSocketUrlString());

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
                AccClientHandler handler = new AfreecaTvChatClientHandler();
                client.addHandler(handler);

                // Wait Connecting
                client.connectBlocking();

                if (client.isOpen()) {
                    try {
                        // Send Initial Packet
                        client.send(AccConstants.createConnectPacket());
                        client.send(AccConstants.createJoinPacket(result));

                        // Repeatedly Sending Ping Packet
                        while (client.getReadyState() == ReadyState.OPEN) {
                            client.send(AccConstants.createPingPacket());
                            Thread.sleep(1000 * 60); // Wait 60 Seconds
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // Run if exception throws or client is not opened
                client.close();
            } catch (InterruptedException | ExecutionException
                     | NoSuchAlgorithmException | KeyManagementException e) {
                e.printStackTrace();
            }
        });
    }

    public void close() {
        if (client != null) {
            client.close();
        }
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

}
