package tk.ethandelany.helpers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tk.ethandelany.tables.counters;

import java.sql.*;
import java.util.Arrays;

public class SQLHelper {
    private static final Logger logger = LoggerFactory.getLogger("GLOBAL");

    // Database Setup Info
    static final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
    static final String DB_URL = ConfigHelper.getConfigString("sql.db_url");
    static final String USER = ConfigHelper.getConfigString("sql.username");
    static final String PASS = ConfigHelper.getConfigString("sql.password");

    public static Connection conn = null;

    public static void setupSQL() {
        try {
            // Register the JDBC driver
            Class.forName(JDBC_DRIVER);

            // Connect to the local database
            logger.info("Connecting to the database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            logger.info("Database connection established!");

            // Ensure the database exists and is actively selected
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS discord;");
            stmt.executeUpdate("USE discord;");
            logger.info("Database configured successfully!");

            counters.setupTable();

        } catch (SQLException | ClassNotFoundException e) {
            logger.error("SQL Setup failed!", e);
        }
    }

    public static Connection getConn() {
        return conn;
    }
}
