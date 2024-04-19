package net.ledestudio.acc.http;

import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;

public class AccHttpRequestResult extends HashMap<AccHttpResponseType, String> {

    public @NotNull URI toWebSocketURI() {
        try {
            return new URI(toWebSocketUrlString());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public @NotNull URL toWebSocketURL() {
        try {
            return new URL(toWebSocketUrlString());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public @NotNull String toWebSocketUrlString() {
        return String.format(
                "wss://%s:%s/Websocket/%s",
                getOrDefault(AccHttpResponseType.CHDOMAIN, ""),
                getOrDefault(AccHttpResponseType.CHPT, ""),
                getOrDefault(AccHttpResponseType.BID, "")
        );
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        this.forEach((key, value) -> sb.append(key).append(":").append(value).append("\n"));
        return sb.toString();
    }

    @Override
    public AccHttpRequestResult clone() {
        return (AccHttpRequestResult) super.clone();
    }

}
