package com.kwizera.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DBInit {
    public static void runSchemaSetup(Connection connection) {
        try {
            String sql = Files.readString(Paths.get("src/main/resources/db/schema.sql"));
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
