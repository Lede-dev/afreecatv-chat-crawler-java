package net.ledestudio.acc.client;

import org.jetbrains.annotations.NotNull;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public interface AccMessage {

    @NotNull String getSenderId();

    @NotNull String getSenderNickname();

    @NotNull String getMessage();

    @NotNull ZonedDateTime getTimestamp(@NotNull ZoneId zone);

}
