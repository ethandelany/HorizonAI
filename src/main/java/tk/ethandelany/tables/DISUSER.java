package tk.ethandelany.tables;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tk.ethandelany.helpers.SQLHelper;

import java.sql.*;
import java.util.Arrays;

public class DISUSER {
    private static final Connection conn = SQLHelper.getConn();
    private static final Logger logger = LoggerFactory.getLogger("GLOBAL");

    public static void checkUser(long uuid, String name) {
        try {
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM DISUSER WHERE UUID = ?");
            pstmt.setLong(1, uuid);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next() == false) {
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO DISUSER (UUID, Username, Clutches, MlgBlocks) VALUES (?, ?, 0, 0)");
                stmt.setLong(1, uuid);
                stmt.setString(2, name);
                stmt.executeUpdate();
                logger.info("[SQL] Didn't find user " + name + " (" + uuid + "), instance has been created for them.");
            } else {
                logger.info("[SQL] Found user " + name + " (" + uuid + ")!");
            }
        } catch(SQLException e) {
            logger.error("[SQL ERROR] Error finding/creating user instance.", e);
        }
    }

    public static void setupTable() {
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("CREATE TABLE if NOT EXISTS DISUSER "
                    + "(UUID BIGINT NOT NULL, "
                    + "Username TEXT, "
                    + "Clutches INTEGER, "
                    + "MlgBlocks INTEGER, "
                    + "PRIMARY KEY ( UUID ));");

            // Add columns for individual counters
            stmt.executeUpdate("ALTER TABLE DISUSER ADD COLUMN if NOT EXISTS Clutches INTEGER NOT NULL DEFAULT 0;");
            stmt.executeUpdate("ALTER TABLE DISUSER ADD COLUMN if NOT EXISTS MlgBlocks INTEGER NOT NULL DEFAULT 0;");
        } catch(SQLException e) {
            logger.error("[SQL ERROR] Error setting up User table.", e);
        }

    }
}
