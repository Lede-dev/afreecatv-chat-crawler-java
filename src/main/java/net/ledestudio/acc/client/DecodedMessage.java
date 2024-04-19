package net.ledestudio.acc.client;

import org.jetbrains.annotations.NotNull;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class DecodedMessage implements AccMessage {

    private final @NotNull String senderId;
    private final @NotNull String senderNickname;
    private final @NotNull String message;

    private final @NotNull ZonedDateTime timestamp = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));

    public DecodedMessage() {
        this.senderId = "";
        this.senderNickname = "";
        this.message = "";
    }

    public DecodedMessage(@NotNull String senderId, @NotNull String senderNickname, @NotNull String message) {
        this.senderId = senderId;
        this.senderNickname = senderNickname;
        this.message = message;
    }

    @Override
    public @NotNull String getSenderId() {
        return senderId;
    }

    @Override
    public @NotNull String getSenderNickname() {
        return senderNickname;
    }

    @Override
    public @NotNull String getMessage() {
        return message;
    }

    @Override
    public @NotNull ZonedDateTime getTimestamp(@NotNull ZoneId zone) {
        return timestamp;
    }
}
