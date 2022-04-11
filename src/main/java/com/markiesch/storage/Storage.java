package com.markiesch.storage;

import com.markiesch.EpicPunishments;
import com.markiesch.controllers.InfractionController;
import com.markiesch.controllers.ProfileController;
import com.markiesch.models.InfractionModel;
import com.markiesch.models.ProfileModel;

import java.util.List;
import java.util.UUID;

public abstract class Storage {
    protected final EpicPunishments plugin;
    protected final InfractionController infractionController;
    protected final ProfileController profileController;

    public Storage(EpicPunishments plugin) {
        this.plugin = plugin;
        this.infractionController = plugin.getInfractionController();
        this.profileController = plugin.getProfileController();
        setup();
    }

    public abstract List<ProfileModel> getProfiles();
    public abstract ProfileModel getProfile(UUID uuid);
    public abstract void createProfile(ProfileModel profile);
    public abstract void saveProfile(ProfileModel profile);
    public abstract List<InfractionModel> loadInfractions(UUID uuid);
    public abstract void saveInfraction(InfractionModel infraction);
    protected abstract void setup();
}