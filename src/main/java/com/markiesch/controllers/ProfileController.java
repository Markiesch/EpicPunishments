package com.markiesch.controllers;

import com.markiesch.EpicPunishments;
import com.markiesch.models.ProfileModel;
import com.markiesch.storage.Query;
import com.markiesch.storage.Storage;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProfileController {
    private final EpicPunishments plugin;
    private final Storage storage;

    public ProfileController(EpicPunishments plugin) {
        this.plugin = plugin;
        storage = plugin.getStorage();
    }

    /**
     * Creates a profile for the given player
     */
    public void createProfile(UUID uuid, String ip) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);

        try {
            System.out.println("Creating profile for " + offlinePlayer.getName() + "...");

            Connection connection = storage.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(Query.ADD_PROFILE.getQuery());
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

            ResultSet result = connection.prepareStatement(Query.SELECT_PROFILES.getQuery()).executeQuery();

            while (result.next()) {
                UUID uuid = UUID.fromString(result.getString("UUID"));
                models.add(new ProfileModel(plugin, uuid));
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

            PreparedStatement preparedStatement = connection.prepareStatement(Query.SELECT_PROFILE.getQuery());
            preparedStatement.setString(1, uuid.toString());

            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();

            model = new ProfileModel(plugin, UUID.fromString(resultSet.getString("UUID")));
            resultSet.close();
        } catch(SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            storage.closeConnection();
        }

        return model;
    }
}
