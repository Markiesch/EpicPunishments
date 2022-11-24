package com.markiesch.modules.profile;

import com.markiesch.storage.Storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProfileController {
    private final Storage storage;

    public ProfileController() {
        storage = Storage.getInstance();
    }

    /**
     * Creates a profile for the given player
     */
    public void createProfile(UUID uuid, String ip) {
        try {
            Connection connection = storage.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(ProfileQuery.ADD_PROFILE);
            preparedStatement.setString(1, uuid.toString());
            preparedStatement.setString(2, ip);
            preparedStatement.setString(3, ip);
            preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            storage.closeConnection();
        }
    }

    public List<ProfileModel> getProfiles() {
        List<ProfileModel> models = new ArrayList<>();

        try {
            Connection connection = storage.getConnection();

            ResultSet result = connection.prepareStatement(ProfileQuery.SELECT_PROFILES).executeQuery();

            while (result.next()) {
                UUID uuid = UUID.fromString(result.getString("UUID"));
                models.add(new ProfileModel(uuid));
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return models;
    }

    public ProfileModel getProfile(UUID uuid) {
        ProfileModel model = null;

        try {
            Connection connection = storage.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(ProfileQuery.SELECT_PROFILE);
            preparedStatement.setString(1, uuid.toString());

            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();

            model = new ProfileModel(UUID.fromString(resultSet.getString("UUID")));
            resultSet.close();
        } catch(SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            storage.closeConnection();
        }

        return model;
    }
}
