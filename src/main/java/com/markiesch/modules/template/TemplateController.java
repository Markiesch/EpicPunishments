package com.markiesch.modules.template;

import com.markiesch.modules.infraction.InfractionType;
import com.markiesch.storage.Storage;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;

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

            templates = ResultSetToModel(result);
        } catch (SQLException sqlException) {
            Bukkit.getLogger().warning("Failed to read from database");
        }

        return templates;
    }

    public @Nullable TemplateModel readSingle(int id) {
        List<TemplateModel> templates = new ArrayList<>();
        try {
            Connection connection = storage.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(TemplateQuery.SELECT_SINGLE_TEMPLATE);
            preparedStatement.setInt(1, id);

            ResultSet result = preparedStatement.executeQuery();

            templates = ResultSetToModel(result);
        } catch (SQLException sqlException) {
            Bukkit.getLogger().warning("Failed to read from database");
        }

        return templates.isEmpty() ? null : templates.get(0);
    }

    public void create(String name, String reason, InfractionType type, Long duration) {
        try {
            Connection connection = storage.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(TemplateQuery.CREATE_TEMPLATE);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, type.name());
            preparedStatement.setString(3, reason);
            preparedStatement.setLong(4, duration);
            preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            Bukkit.getLogger().warning("Failed to write to database");
        }
    }

    public void delete(Integer id) {
        try {
            Connection connection = storage.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(TemplateQuery.DELETE_TEMPLATE);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            Bukkit.getLogger().warning("Failed to write to database");
        }
    }

    public void updateTemplate(int id, String name, InfractionType type, String reason, long duration) {
        try {
            Connection connection = storage.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(TemplateQuery.UPDATE_TEMPLATE);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, type.name());
            preparedStatement.setString(3, reason);
            preparedStatement.setLong(4, duration);
            preparedStatement.setInt(5, id);
            preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            Bukkit.getLogger().warning("Failed to write to database");
        }
    }

    private List<TemplateModel> ResultSetToModel(ResultSet result) throws SQLException {
        List<TemplateModel> templates = new ArrayList<>();
        while (result.next()) {
            int templateId = result.getInt("id");
            String templateName = result.getString("name");
            String templateReason = result.getString("reason");
            InfractionType templateType = InfractionType.valueOf(result.getString("type"));
            Long templateDuration = result.getLong("duration");
            templates.add(new TemplateModel(templateId, templateName, templateReason, templateType, templateDuration));
        }
        return templates;
    }
}
