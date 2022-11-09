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

    public TemplateModel readSingle(int id) {
        List<TemplateModel> templates = new ArrayList<>();
        try {
            Connection connection = storage.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(TemplateQuery.SELECT_SINGLE_TEMPLATE);
            preparedStatement.setInt(1, id);

            ResultSet result = connection.prepareStatement(TemplateQuery.SELECT_ALL_TEMPLATES).executeQuery();

            while (result.next()) {
                int templateId = result.getInt("id");
                String templateName = result.getString("name");
                String templateReason = result.getString("reason");
                String templateType = result.getString("type");
                Long templateDuration = result.getLong("duration");
                templates.add(new TemplateModel(templateId, templateName, templateReason, templateType, templateDuration));
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return templates.size() > 0 ? templates.get(0) : null;
    }

    public void create(String name, String reason, String type, Long duration) {
        try {
            Connection connection = storage.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(TemplateQuery.CREATE_TEMPLATE);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, type);
            preparedStatement.setString(3, reason);
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

    public void updateTemplate(int id, String name, String type, String reason, long duration) {
        try {
            Connection connection = storage.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(TemplateQuery.UPDATE_TEMPLATE);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, type);
            preparedStatement.setString(3, reason);
            preparedStatement.setLong(4, duration);
            preparedStatement.setInt(5, id);
            preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            storage.closeConnection();
        }
    }
}
