package net.ledestudio.acc.util;

import net.ledestudio.acc.http.AccHttpRequestResult;
import net.ledestudio.acc.http.AccHttpResponseType;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;

public class AccConstants {

    public static final int PING_PACKET_PERIOD_SECONDS = 60;

    public static final String F = "\u000c";
    public static final String ESC = "\u001b\t";

    public static String createConnectPacket() {
        return String.format("%s000100000600%s16%s", ESC, F.repeat(3), F);
    }

    public static String createJoinPacket(@NotNull AccHttpRequestResult result) {
        final String chat = result.get(AccHttpResponseType.CHATNO);
        return String.format("%s0002%06d00%s%s%s", ESC, getByteSize(chat), F, chat, F.repeat(5));
    }

    public static String createPingPacket() {
        return String.format("%s000000000100%s", ESC, F);
    }

    private static int getByteSize(@NotNull String text) {
        return new String(text.getBytes(), StandardCharsets.UTF_8).length() + 6;
    }

}
