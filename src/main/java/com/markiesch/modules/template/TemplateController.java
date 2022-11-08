package com.markiesch.modules.template;

import com.markiesch.storage.Storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TemplateController {
    private final Storage storage;

    public TemplateController() {
        storage = Storage.getInstance();
    }

    public List<TemplateModel> readAll() {
        List<TemplateModel> templates = new ArrayList<>();

        try {
            Connection connection = storage.getConnection();
            ResultSet result = connection.prepareStatement(TemplateQuery.SELECT_ALL_TEMPLATES).executeQuery();

            while (result.next()) {
                int id = result.getInt("id");
                String name = result.getString("name");
                String reason = result.getString("reason");
                String type = result.getString("type");
                Long duration = result.getLong("duration");
                templates.add(new TemplateModel(id, name, reason, type, duration));
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return templates;
    }

    public void addTemplate(String name, String reason, String type, Long duration) {
        try {
            Connection connection = storage.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(TemplateQuery.CREATE_TEMPLATE);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, reason);
            preparedStatement.setString(3, type);
            preparedStatement.setLong(4, duration);
            preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            storage.closeConnection();
        }
    }

    public void delete(Integer id) {
        try {
            Connection connection = storage.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(TemplateQuery.DELETE_TEMPLATE);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            storage.closeConnection();
        }
    }
}
