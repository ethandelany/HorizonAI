package tk.ethandelany.helpers;

import org.simpleyaml.configuration.file.YamlFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.ConfigurationException;
import java.io.IOException;

public class ConfigHelper {

    private static final Logger logger = LoggerFactory.getLogger("GLOBAL");
    static YamlFile config = new YamlFile("config.yml");

    public static void setupConfig() throws ConfigurationException {

        try {
            if (!config.exists()) {
                logger.info("New file has been created: " + config.getFilePath());
                config.createNewFile(true);
                throw new ConfigurationException("Config requires first-time setup to run the bot.");
            } else {
                logger.info(config.getFilePath() + " already exists, loading configurations...");
            }
            config.loadWithComments();
            logger.info("Config loaded!");
        } catch (ConfigurationException ce) {
            throw ce;
        } catch (Exception e) {
            logger.error("Config loading failed!", e);
        }

        config.addDefault("bot.token", "tokengoeshere");
        config.addDefault("sql.db_url", "jdbc:mariadb://localhost");
        config.addDefault("sql.username", "username");
        config.addDefault("sql.password", "password");

        try {
            config.saveWithComments();
            logger.info("Config saved successfully.");
        } catch (IOException e) {
            logger.error("Config saving failed!", e);
        }
    }

    public static String getConfigString(String path) {
        return config.getString(path);
    }
}
