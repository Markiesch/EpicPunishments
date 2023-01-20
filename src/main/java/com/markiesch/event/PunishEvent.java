package com.markiesch.event;

import com.markiesch.modules.infraction.InfractionModel;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PunishEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final InfractionModel infractionModel;

    public PunishEvent(InfractionModel infractionModel) {
        this.infractionModel = infractionModel;
    }

    public InfractionModel getInfraction() {
        return infractionModel;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
