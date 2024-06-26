package net.ledestudio.acc.http;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AccHttpRequester {

    private static final Logger LOGGER = Logger.getLogger(AccHttpRequester.class.getSimpleName());

    private final @NotNull String bid;
    private final @NotNull String bno;

    public AccHttpRequester(@NotNull String bid, @NotNull String bno) {
        this.bid = bid;
        this.bno = bno;
    }

    public AccHttpRequester(@NotNull String afreecaTvLiveUrl) {
        final String baseUrl = "play.afreecatv.com";
        int index = afreecaTvLiveUrl.indexOf(baseUrl);
        if (index == -1) {
            throw new IllegalArgumentException("Invalid URL: " + afreecaTvLiveUrl);
        }

        String params = afreecaTvLiveUrl.substring(index + baseUrl.length() + 1);
        String[] split = params.split("/");
        if (split.length != 2) {
            throw new IllegalArgumentException("Invalid URL: " + afreecaTvLiveUrl);
        }

        this.bid = split[0];
        this.bno = split[1];
    }

    public @NotNull CompletableFuture<@Nullable AccHttpRequestResult> request() {
        return CompletableFuture.supplyAsync(() -> {
            String url = "https://live.afreecatv.com/afreeca/player_live_api.php";

            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost post = new HttpPost(String.format("%s?bjid=%s", url, bid));

            AccHttpRequestParam param = new AccHttpRequestParam(bid, bno);
            post.addHeader("content-type", "application/x-www-form-urlencoded");
            post.setEntity(param.toStringEntity());

            try {
                HttpResponse response = client.execute(post);
                HttpEntity entity = response.getEntity();
                String responseStr = EntityUtils.toString(entity, StandardCharsets.UTF_8);

                JsonObject root = JsonParser.parseString(responseStr).getAsJsonObject();
                if (root == null || !root.has("CHANNEL")) {
                    return null;
                }

                JsonObject channel = root.getAsJsonObject("CHANNEL");
                if (channel == null || !channel.has("CHDOMAIN")) {
                    return null;
                }

                // mapping result
                AccHttpRequestResult result = new AccHttpRequestResult();
                result.put(AccHttpResponseType.CHDOMAIN, channel.get("CHDOMAIN").getAsString().toLowerCase());
                result.put(AccHttpResponseType.CHATNO, channel.get("CHATNO").getAsString());
                result.put(AccHttpResponseType.FTK, channel.get("FTK").getAsString());
                result.put(AccHttpResponseType.TITLE, channel.get("TITLE").getAsString());
                result.put(AccHttpResponseType.BJID, channel.get("BJID").getAsString());
                result.put(AccHttpResponseType.CHPT, Integer.toString(channel.get("CHPT").getAsInt() + 1));
                result.put(AccHttpResponseType.BID, bid);
                result.put(AccHttpResponseType.BNO, bno);
                return result;
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "An error occurred during HTTP request execution: ", e);
            } finally {
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        });
    }

    public @NotNull String getBID() {
        return bid;
    }

    public @NotNull String getBNO() {
        return bno;
    }

}
