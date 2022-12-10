package com.markiesch.modules.profile;

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

    private static class ProfileManagerHolder {
        public static final ProfileManager INSTANCE = new ProfileManager();
    }

    public static ProfileManager getInstance() {
        return ProfileManagerHolder.INSTANCE;
    }

    public ProfileModel getPlayer(UUID uuid) {
        return profileModelMap.get(uuid);
    }

    public List<ProfileModel> getPlayers() {
        return new ArrayList<>(profileModelMap.values());
    }
}
