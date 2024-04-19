package net.ledestudio.acc.client;

import net.ledestudio.acc.http.AccHttpRequestResult;
import net.ledestudio.acc.util.AccConstants;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AccMessageDecoder {

    private final @NotNull AccHttpRequestResult result;
    private final @NotNull String textToDecode;

    public AccMessageDecoder(@NotNull AccHttpRequestResult result, @NotNull String textToDecode) {
        this.result = result;
        this.textToDecode = textToDecode;
    }

    public @Nullable DecodedMessage decode() {
        String[] parts = textToDecode.split(AccConstants.F);

        if (parts.length < 7) {
            return null;
        }

        if (parts[1].equals("-1") || parts[1].equals("1") || parts[1].contains("|") ||
                parts[2].isEmpty() || parts[2].startsWith("fw=")) {
            return null;
        }

        String senderId = parts[2];
        String senderNickname = parts[6];
        String message = parts[1];
        return new DecodedMessage(result, senderId, senderNickname, message);
    }

}
