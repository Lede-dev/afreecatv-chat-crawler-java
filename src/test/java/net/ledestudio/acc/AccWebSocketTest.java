package net.ledestudio.acc;

import com.google.common.collect.Maps;
import net.ledestudio.acc.client.AccClient;
import net.ledestudio.acc.http.AccHttpRequestResult;
import net.ledestudio.acc.http.AccHttpRequester;
import net.ledestudio.acc.service.AfreecaTvChatCrawler;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.junit.jupiter.api.Test;

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
        AfreecaTvChatCrawler crawler = new AfreecaTvChatCrawler("https://play.afreecatv.com/lilpa0309/263094474");
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
