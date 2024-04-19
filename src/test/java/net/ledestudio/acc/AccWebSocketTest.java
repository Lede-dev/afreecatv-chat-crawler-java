package net.ledestudio.acc;

import net.ledestudio.acc.client.AccClient;
import net.ledestudio.acc.client.AccMessage;
import net.ledestudio.acc.http.AccHttpRequestResult;
import net.ledestudio.acc.http.AccHttpRequester;
import net.ledestudio.acc.service.AfreecaTvChatCrawler;
import net.ledestudio.acc.service.AfreecaTvMessageReceiveEvent;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
public class AccWebSocketTest {

    @Test
    public void accWebSocketClientConnectTest() throws ExecutionException, InterruptedException {
        AccHttpRequester requester = new AccHttpRequester("https://play.afreecatv.com/seokwngud/262914674");
        CompletableFuture<AccHttpRequestResult> future = requester.request();
        WebSocketClient client = new AccClient(future.get(), new Draft_6455());
        client.connectBlocking();
    }

    @Test
    public void afreecaTvChatCrawlerUrlConstructorTest() throws InterruptedException {
        AfreecaTvChatCrawler crawler = new AfreecaTvChatCrawler(
                "https://play.afreecatv.com/lilpa0309/263094474", true, 1000);
        crawler.registerMessageReceiveEvent(new AfreecaTvMessageReceiveEvent() {
            @Override
            public void onMessageReceive(@NotNull AccMessage message) {
                System.out.printf(
                        "%s | %s[%s] : %s%n",
                        message.getTimestamp().format(DateTimeFormatter.ISO_LOCAL_TIME), message.getSenderNickname(),
                        message.getSenderId(), message.getMessage()
                );
            }
        });
        crawler.connect();

        while (true) {
            Thread.sleep(1000);
        }
    }

    @Test
    public void afreecaTvChatCrawlerBidBnoConstructorTest() throws InterruptedException {
        AfreecaTvChatCrawler crawler = new AfreecaTvChatCrawler("seokwngud", 262914674);
        crawler.connect();
        Thread.sleep(1000 * 5);
    }

}
