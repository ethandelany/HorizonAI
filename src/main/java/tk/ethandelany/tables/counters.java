package tk.ethandelany.tables;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tk.ethandelany.helpers.SQLHelper;

import java.sql.*;
import java.util.Arrays;

public class counters {
    private static final Connection conn = SQLHelper.getConn();
    private static final Logger logger = LoggerFactory.getLogger("GLOBAL");

    public static Boolean modifyIndCounter(String counter, String username, long uuid, String operation, int amount) {
        try {
            checkUser(uuid, username);

            // Actually update the counter integer
            PreparedStatement stmt = conn.prepareStatement("UPDATE counters" +
                    " SET " + counter + " = " + counter + " " + operation + " ?" +
                    " WHERE uuid = ?;");
            stmt.setInt(1, amount);
            stmt.setLong(2, uuid);
            stmt.executeUpdate();

            // Update the username field if the user has changed their name
            PreparedStatement stmt2 = conn.prepareStatement("UPDATE counters" +
                    " SET username = ?" +
                    " WHERE uuid = ?;");
            stmt2.setString(1, username);
            stmt2.setLong(2, uuid);
            stmt2.executeUpdate();

            logger.info("[SQL] Modified " + username + " (" + uuid + ") by: " + counter + " " + operation + " " + amount + ".");
            return true;
        } catch(SQLException e) {
            logger.error("[SQL ERROR] Error modifying " + counter + " for " + username + " (" + uuid + ").", e);
            return false;
        }
    }

    public static void checkUser(long uuid, String name) {
        try {
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM counters WHERE uuid = ?");
            pstmt.setLong(1, uuid);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next() == false) {
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO counters (uuid, username, clutches, mlg_blocks) VALUES (?, ?, 0, 0)");
                stmt.setLong(1, uuid);
                stmt.setString(2, name);
                stmt.executeUpdate();
                logger.info("[SQL] Didn't find user " + name + " (" + uuid + "), row has been created for them.");
            } else {
                logger.info("[SQL] Found user " + name + " (" + uuid + ")!");
            }
        } catch(SQLException e) {
            logger.error("[SQL ERROR] Error finding/creating user row.", e);
        }
    }

    public static void setupTable() {
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("CREATE TABLE if NOT EXISTS counters "
                    + "(uuid BIGINT NOT NULL, "
                    + "username TEXT, "
                    + "clutches INTEGER, "
                    + "mlg_blocks INTEGER, "
                    + "PRIMARY KEY ( uuid ));");

            // Add columns for individual counters
            stmt.executeUpdate("ALTER TABLE counters ADD COLUMN if NOT EXISTS clutches INTEGER NOT NULL DEFAULT 0;");
            stmt.executeUpdate("ALTER TABLE counters ADD COLUMN if NOT EXISTS mlg_blocks INTEGER NOT NULL DEFAULT 0;");
        } catch(SQLException e) {
            logger.error("[SQL ERROR] Error setting up clutches table.", e);
        }

    }
}
