package com.kwizera.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DBInit {
    public static void runSchemaSetup(Connection connection) {
        try {
            InputStream input = DBInit.class.getClassLoader().getResourceAsStream("schema.sql");
            if (input == null) {
                CustomLogger.log(CustomLogger.LogLevel.ERROR, "Schema file not found");
                throw new IOException("Schema file not found");
            }
            String sql = new String(input.readAllBytes(), StandardCharsets.UTF_8);
            Statement stmt = connection.createStatement();
            stmt.execute(sql);
            CustomLogger.log(CustomLogger.LogLevel.INFO, "Database migration completed");
        } catch (IOException | SQLException e) {
            if (e instanceof IOException) {
                CustomLogger.log(CustomLogger.LogLevel.ERROR, "Unable to load the schema file");
            } else {
                CustomLogger.log(CustomLogger.LogLevel.ERROR, "Schema file execution failed");
            }
            throw new RuntimeException(e);
        }
    }
}
