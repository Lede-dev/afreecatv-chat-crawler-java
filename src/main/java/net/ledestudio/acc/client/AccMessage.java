package net.ledestudio.acc.client;

import org.jetbrains.annotations.NotNull;

import java.time.ZonedDateTime;

public interface AccMessage {

    @NotNull String getSenderId();

    @NotNull String getSenderNickname();

    @NotNull String getMessage();

    @NotNull ZonedDateTime getTimestamp();

    @NotNull String getBroadcasterID();

    @NotNull String getBroadcasterNumber();

    @NotNull String getLiveBroadcastURL();

    @NotNull String getLiveBroadcastTitle();

}
