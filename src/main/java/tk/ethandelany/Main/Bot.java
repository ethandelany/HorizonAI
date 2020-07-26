package tk.ethandelany.Main;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tk.ethandelany.helpers.CommandHelper;
import tk.ethandelany.helpers.ConfigHelper;
import tk.ethandelany.helpers.SQLHelper;

import javax.security.auth.login.LoginException;
import java.util.Arrays;
import java.util.List;

public class Bot {
    private static final Logger logger = LoggerFactory.getLogger("GLOBAL");
    private static final List<GatewayIntent> intents = Arrays.asList(
            GatewayIntent.GUILD_MEMBERS,
            GatewayIntent.GUILD_BANS,
            GatewayIntent.GUILD_EMOJIS,
            GatewayIntent.GUILD_INVITES,
            GatewayIntent.GUILD_VOICE_STATES,
            GatewayIntent.GUILD_PRESENCES,
            GatewayIntent.GUILD_MESSAGES,
            GatewayIntent.GUILD_MESSAGE_REACTIONS,
            GatewayIntent.GUILD_MESSAGE_TYPING,
            GatewayIntent.DIRECT_MESSAGES,
            GatewayIntent.DIRECT_MESSAGE_REACTIONS,
            GatewayIntent.DIRECT_MESSAGE_TYPING
    );

    public static void main(String[] args) {

        ConfigHelper.setupConfig();
        try {
            JDA api = JDABuilder.create(ConfigHelper.getConfigString("bot.token"), intents)
                    .addEventListeners(new CommandHelper())
                    .build();
        } catch (LoginException e) {
            logger.error("Failed to login! Check the validity of your bot token.");
        }
        SQLHelper.setupSQL();
    }
}
