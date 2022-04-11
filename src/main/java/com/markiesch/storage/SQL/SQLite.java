package com.markiesch.storage.SQL;

import com.google.common.base.Function;
import com.markiesch.EpicPunishments;
import com.markiesch.models.InfractionModel;
import com.markiesch.models.ProfileModel;
import com.markiesch.storage.Storage;
import org.bukkit.command.ProxiedCommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class SQLite extends Storage {
    private final File file;
    private Connection connection;

    public SQLite(EpicPunishments plugin) {
        super(plugin);
        this.file = new File(plugin.getDataFolder(), "data.db");
    }

    @Override
    protected void setup() {
        try {
            connection = getConnection();

            // Create tables
            executeUpdate(Query.CREATE_INFRACTION_TABLE);
            executeUpdate(Query.CREATE_PLAYER_TABLE);
            executeUpdate(Query.CREATE_TEMPLATE_TABLE);

            ResultSet profileResult = connection.prepareStatement("SELECT * FROM Profiles").executeQuery();
            while (profileResult.next()) {
                UUID uuid = UUID.fromString(profileResult.getString("UUID"));
                System.out.println("Loading profile " + uuid);
                profileController.createProfile(uuid);
            }
            profileResult.close();

            ResultSet infractionResult = connection.prepareStatement("SELECT * FROM Profiles").executeQuery();
            while (infractionResult.next()) {
                UUID uuid = UUID.fromString(infractionResult.getString("UUID"));
                System.out.println("Loading profile " + uuid);
                profileController.createProfile(uuid);
            }
            infractionResult.close();

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    /**
     * Executes an update query to the (local) database
     * @param query The query that needs to be executed
     */
    private void executeUpdate(Query query) {
        try {
            Connection connection = getConnection();
            connection.prepareStatement(query.getQuery()).executeUpdate();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    @Override public List<ProfileModel> getProfiles() {
        List<ProfileModel> models = new ArrayList<>();

        try {
            ResultSet result = getConnection().prepareStatement(Query.SELECT_PROFILES.getQuery()).executeQuery();

            while (result.next()) {
                UUID uuid = UUID.fromString(result.getString("UUID"));
                models.add(new ProfileModel(plugin, uuid));
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return models;
    }

    @Override public ProfileModel getProfile(UUID uuid) {
        ProfileModel model = null;

        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(Query.SELECT_PROFILE.getQuery());
            preparedStatement.setString(1, uuid.toString());

            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();

            model = new ProfileModel(plugin, UUID.fromString(resultSet.getString("UUID")));
        } catch(SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return model;
    }

    @Override
    public List<InfractionModel> loadInfractions(UUID uuid) {
        List<InfractionModel> result = new ArrayList<>();

        try {
            openConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM ");
            statement.executeQuery();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            closeConnection();
        }

        return result;
    }

    @Override
    public void createProfile(ProfileModel profile) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(Query.ADD_PROFILE.getQuery());
            preparedStatement.setString(1, profile.uuid.toString());

            preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    @Override
    public void saveProfile(ProfileModel profile) {
        //        try {
        ////            PreparedStatement preparedStatement = getConnection().prepareStatement(Query.SAVE_PROFILE.getQuery());
        ////            preparedStatement.setString(1, profile.uuid.toString());
        //
        //
        //        } catch (SQLException sqlException) {
        //            sqlException.printStackTrace();
        //        }
    }

    @Override
    public void saveInfraction(InfractionModel infraction) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "REPLACE INTO Infractions (UUID, Victim, Issuer, Type, Reason, Duration, Date)" +
                            "VALUES (?, ?, ?, ?, ?, ?, ?);"
            );

            // Insert model values
            preparedStatement.setString(1, infraction.uuid.toString());
            preparedStatement.setString(1, infraction.victim.toString());
            preparedStatement.setString(1, infraction.issuer.toString());
            preparedStatement.setString(1, infraction.type.toString());
            preparedStatement.setString(1, infraction.reason);
            preparedStatement.setLong(1, infraction.duration);
            preparedStatement.setLong(1, infraction.date.getTime());
            preparedStatement.executeQuery();

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    private Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) openConnection();
        return connection;
    }

    private void openConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + file);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } catch (ClassNotFoundException classNotFoundException) {
            plugin.getLogger().severe(classNotFoundException.getMessage());
        }
    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
}
