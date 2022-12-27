package com.markiesch.modules.profile;

import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ProfileManager {
    private final Map<UUID, ProfileModel> profileModelMap;
    private final ProfileController profileController;

    private ProfileManager() {
        profileModelMap = new HashMap<>();
        profileController = new ProfileController();
    }

    public void initialize() {
        List<ProfileModel> profileList = profileController.getProfiles();

        for (ProfileModel profileModel : profileList) {
            profileModelMap.put(profileModel.uuid, profileModel);
        }
    }

    public boolean handlePlayerLogin(UUID uuid, String name, String hostAddress) {
        boolean success = profileController.createProfile(uuid, name, hostAddress);
        if (!success) return false;

        profileModelMap.put(uuid, new ProfileModel(uuid, name, hostAddress, null));
        return true;
    }


    private static class ProfileManagerHolder {
        public static final ProfileManager INSTANCE = new ProfileManager();
    }

    public static ProfileManager getInstance() {
        return ProfileManagerHolder.INSTANCE;
    }

    public @Nullable ProfileModel getPlayer(UUID uuid) {
        return profileModelMap.get(uuid);
    }

    public @Nullable ProfileModel getPlayer(String name) {
        return profileModelMap
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue().name.equals(name))
                .findFirst()
                .map(Map.Entry::getValue)
                .orElse(null);
    }


    public List<ProfileModel> getPlayers() {
        return new ArrayList<>(profileModelMap.values());
    }
}
