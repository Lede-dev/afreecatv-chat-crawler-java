package net.ledestudio.acc.client;

import net.ledestudio.acc.util.AccConstants;
import org.jetbrains.annotations.Nullable;

public class AccMessageDecoder {

    private final String textToDecode;

    public AccMessageDecoder(String textToDecode) {
        this.textToDecode = textToDecode;
    }

    public @Nullable DecodedMessage decode() {
        String[] parts = textToDecode.split(AccConstants.F);
        if (parts.length > 5 && !parts[1].equals("-1") && !parts[1].equals("1") && !parts[1].contains("|")) {
            String senderId = parts[2];
            String senderNickname = parts[6];
            String message = parts[1];
            return new DecodedMessage(senderId, senderNickname, message);
        }
        return null;
    }

}
