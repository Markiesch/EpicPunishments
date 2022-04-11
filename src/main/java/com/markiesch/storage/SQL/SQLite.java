package com.markiesch.storage.SQL;

import com.markiesch.EpicPunishments;
import com.markiesch.models.InfractionModel;
import com.markiesch.models.ProfileModel;
import com.markiesch.storage.Storage;

import java.io.File;
import java.io.IOException;
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
        setup();
    }

    private void setup() {
        try {
            connection = getConnection();

            // Create tables
            System.out.println("Creating SQLite tables...");
            executeUpdate(Query.CREATE_INFRACTION_TABLE);
            executeUpdate(Query.CREATE_PLAYER_TABLE);
            executeUpdate(Query.CREATE_TEMPLATE_TABLE);
            System.out.println("Created SQLite tables!");

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            closeConnection();
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
            resultSet.close();
        } catch(SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            closeConnection();
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

            // Already exists
            if (getProfile(profile.uuid) != null)
            {
                System.out.println(profile.getPlayer().getName() + " already has a profile");
                return;
            }

            System.out.println("Creating profile for " + profile.getPlayer().getName());
            System.out.println();

            PreparedStatement preparedStatement = getConnection().prepareStatement(Query.ADD_PROFILE.getQuery());
            preparedStatement.setString(1, profile.uuid.toString());
//            preparedStatement.setString(2, profile.uuid.toString());

            preparedStatement.executeUpdate();
            closeConnection();
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
            PreparedStatement preparedStatement = getConnection().prepareStatement(
                    "REPLACE INTO Infraction (Victim, Issuer, Type, Reason, Duration, Date)" +
                            "VALUES (?, ?, ?, ?, ?, ?);"
            );

            // Insert model values
            preparedStatement.setString(1, infraction.victim.toString());
            preparedStatement.setString(2, infraction.issuer.toString());
            preparedStatement.setString(3, infraction.type.toString());
            preparedStatement.setString(4, infraction.reason);
            preparedStatement.setLong(5, infraction.duration);
            preparedStatement.setLong(6, infraction.date.getTime());
            preparedStatement.executeUpdate();

            closeConnection();
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
            if (!file.exists()) {
                file.createNewFile();
            }

            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());
        } catch (SQLException | IOException | ClassNotFoundException exception) {
            exception.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) connection.close();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
}
