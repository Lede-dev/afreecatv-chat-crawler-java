package net.ledestudio.acc.http;

import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;

public class AccHttpRequestResult extends HashMap<AccHttpResponseType, String> {

    public @NotNull URI toWebSocketURI() throws URISyntaxException {
        return new URI(toWebSocketUrlString());
    }

    public @NotNull URL toWebSocketURL() throws MalformedURLException {
        return new URL(toWebSocketUrlString());
    }

    public @NotNull String toWebSocketUrlString() {
        return String.format(
                "wss://%s:%s/Websocket/%s",
                getOrDefault(AccHttpResponseType.CHDOMAIN, ""),
                getOrDefault(AccHttpResponseType.CHPT, ""),
                getOrDefault(AccHttpResponseType.BID, "")
        );
    }

}
