package com.markiesch.controllers;

import com.markiesch.EpicPunishments;
import com.markiesch.models.InfractionModel;
import com.markiesch.storage.Storage;
import com.markiesch.utils.PunishTypes;

import java.util.*;
import java.util.stream.Collectors;

public class InfractionController {
    private final Storage storage;
    private final HashMap<UUID, InfractionModel> infractions;

    public InfractionController(EpicPunishments plugin) {
        this.storage = plugin.getStorage();
        infractions = new HashMap<>();
    }

    public void create(PunishTypes type, UUID victim, UUID issuer, String reason, long duration, Date date) {
        UUID uuid = UUID.randomUUID();
        InfractionModel infraction = new InfractionModel(uuid, type, victim, issuer, reason, duration, date);
        add(infraction);
    }

    public List<InfractionModel> getPlayerInfractions(UUID uuid) {
        return infractions.values()
                .stream()
                .filter(infraction -> uuid.equals(infraction.victim))
                .collect(Collectors.toList());
    }

    public void add(InfractionModel infraction) {
        infractions.put(infraction.uuid, infraction);
        storage.saveInfraction(infraction);
    }
}
