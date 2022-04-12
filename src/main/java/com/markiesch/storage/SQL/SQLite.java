package com.markiesch.storage.SQL;

import com.markiesch.models.ProfileModel;

import java.util.ArrayList;
import java.util.List;

public class SQLite {
    public List<ProfileModel> getProfiles() {
        List<ProfileModel> models = new ArrayList<>();

//        try {
//            ResultSet result = getConnection().prepareStatement(Query.SELECT_PROFILES.getQuery()).executeQuery();

//            while (result.next()) {
//                UUID uuid = UUID.fromString(result.getString("UUID"));
//                models.add(new ProfileModel(plugin, uuid));
//            }
//        } catch (SQLException sqlException) {
//            sqlException.printStackTrace();
//        }

        return models;
    }
}
