package com.kwizera.config;

import com.kwizera.utils.CustomLogger;
import com.kwizera.utils.DBInit;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.sql.Connection;

@WebListener
public class AppInitializer implements ServletContextListener {
    private HikariDataSource dataSource;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        try {
            Dotenv dotenv = Dotenv.load();
            String URL = dotenv.get("DB_URL");
            String USER = dotenv.get("DB_USER");
            String PASSWORD = dotenv.get("DB_PASSWORD");

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(URL);
            config.setDriverClassName("org.postgresql.Driver");
            config.setUsername(USER);
            config.setPassword(PASSWORD);
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(2);

            dataSource = new HikariDataSource(config);
            event.getServletContext().setAttribute("DATA_SOURCE", dataSource);
            CustomLogger.log(CustomLogger.LogLevel.INFO, "HikariCP datasource initialized");

            Connection connection = dataSource.getConnection();
            DBInit.runSchemaSetup(connection);

        } catch (Exception e) {
            CustomLogger.log(CustomLogger.LogLevel.ERROR, "Unable to initialize HikariCP datasource. " + e.getMessage());
            throw new RuntimeException("Unable to initialize HikariCP datasource");
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        if (dataSource != null) dataSource.close();
    }
}
