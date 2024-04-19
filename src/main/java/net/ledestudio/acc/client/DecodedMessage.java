package net.ledestudio.acc.client;

import net.ledestudio.acc.http.AccHttpRequestResult;
import net.ledestudio.acc.http.AccHttpResponseType;
import org.jetbrains.annotations.NotNull;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class DecodedMessage implements AccMessage {

    private final @NotNull AccHttpRequestResult result;

    private final @NotNull String senderId;
    private final @NotNull String senderNickname;
    private final @NotNull String message;

    private final @NotNull ZonedDateTime timestamp = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));

    public DecodedMessage(@NotNull AccHttpRequestResult result) {
        this.result = result;
        this.senderId = "";
        this.senderNickname = "";
        this.message = "";
    }

    public DecodedMessage(@NotNull AccHttpRequestResult result, @NotNull String senderId, @NotNull String senderNickname, @NotNull String message) {
        this.result = result;
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
    public @NotNull ZonedDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public @NotNull String getBroadcasterID() {
        return result.get(AccHttpResponseType.BID);
    }

    @Override
    public @NotNull String getBroadcasterNumber() {
        return result.get(AccHttpResponseType.BNO);
    }

    @Override
    public @NotNull String getLiveBroadcastURL() {
        return String.format("play.afreecatv.com/%s/%s", getBroadcasterID(), getBroadcasterNumber());
    }

    @Override
    public @NotNull String getLiveBroadcastTitle() {
        return result.get(AccHttpResponseType.TITLE);
    }
}
