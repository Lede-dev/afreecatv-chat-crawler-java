package net.ledestudio.acc.http;

import com.google.common.collect.Maps;
import org.apache.http.entity.StringEntity;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class AccHttpRequestParam {

    private final Map<String, String> dataMap = Maps.newHashMap();

    public AccHttpRequestParam(@NotNull String bid, @NotNull String bno) {
        init(bid, bno);
    }

    public void init(@NotNull String bid, @NotNull String bno) {
        dataMap.clear();
        dataMap.put("bid", bid);
        dataMap.put("bno", bno);
        dataMap.put("type", "live");
        dataMap.put("confirm_adult", "false");
        dataMap.put("player_type", "html5");
        dataMap.put("mode", "landing");
        dataMap.put("from_api", "0");
        dataMap.put("pwd", "");
        dataMap.put("stream_type", "common");
        dataMap.put("quality", "HD");
    }

    public StringEntity toStringEntity() {
        StringBuilder params = new StringBuilder();
        for (Map.Entry<String, String> entry : dataMap.entrySet()) {
            if (params.length() != 0) {
                params.append('&');
            }
            params.append(entry.getKey());
            params.append('=');
            params.append(entry.getValue());
        }
        return new StringEntity(params.toString(), StandardCharsets.UTF_8);
    }

}
