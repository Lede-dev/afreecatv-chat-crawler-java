package net.ledestudio.acc.service;

import net.ledestudio.acc.client.AccMessage;
import org.jetbrains.annotations.NotNull;

public abstract class AfreecaTvMessageReceiveEvent {

    public abstract void onMessageReceive(@NotNull AccMessage message);

}
