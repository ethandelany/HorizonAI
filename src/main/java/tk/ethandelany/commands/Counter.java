package tk.ethandelany.commands;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tk.ethandelany.helpers.SQLHelper;
import tk.ethandelany.tables.DISUSER;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Counter {

    private static final Connection conn = SQLHelper.getConn();
    private static final Logger logger = LoggerFactory.getLogger("GLOBAL");

    public static void counterStandard(String[] message, GuildMessageReceivedEvent event) {
        // Validating the operation and the simple operation (for response and log)
        String operation;
        String opSimp;
        if(message[1].equalsIgnoreCase("add")) {
            operation = "+";
            opSimp = "increased";
        } else if(message[1].equalsIgnoreCase("sub")) {
            operation = "-";
            opSimp = "decreased";
        } else {
            event.getChannel().sendMessage("Incorrect operation. Try (add/sub)").queue();
            return;
        }

        // Ensuring the amount is actually an integer
        int amount;
        try {
            amount = Integer.parseInt(message[2]);
        } catch (NumberFormatException e) {
            event.getChannel().sendMessage("Incorrect number. Try (any number)").queue();
            return;
        }

        // Validating the counter
        String counter;
        if(message[3].equalsIgnoreCase("clutches")) {
            counter = "clutches";
        } else if(message[3].equalsIgnoreCase("mlgblocks")) {
            counter = "mlgblocks";
        } else {
            event.getChannel().sendMessage("Incorrect counter. Try (clutches/mlgblocks)").queue();
            return;
        }

        // Ensuring they actually @ed a user in the Discord
        try {
            event.getGuild().getMemberById(message[4].substring(3, message[4].length() - 1)).getUser();
        } catch (StringIndexOutOfBoundsException|NullPointerException e) {
            event.getChannel().sendMessage("Invalid user. Try @ing someone").queue();
            return;
        }
        // Getting the correct form of the user for the database (target) and the message (tarSimp)
        String target = event.getGuild().getMemberById(message[4].substring(3, message[4].length() - 1)).getUser().getAsTag();
        long uuid = event.getGuild().getMemberById(message[4].substring(3, message[4].length() - 1)).getUser().getIdLong();
        String tarTag = message[4];

        // Actually executing the counter command
        modifyIndCounter(counter, target, uuid, operation, amount, event, opSimp, tarTag);
    }

    public static void counterSimple(String[] message, GuildMessageReceivedEvent event) {
        String operation = "+";
        String opSimp = "increased";
        int amount = 1;
        String counter;
        if(message[0].equalsIgnoreCase("$clutches++")) {
            counter = "clutches";
        } else if(message[0].equalsIgnoreCase("$mlgblocks++")) {
            counter = "mlgblocks";
        } else {
            event.getChannel().sendMessage("Incorrect counter. Try (clutches/mlgblocks)").queue();
            return;
        }

        // Ensuring they actually @ed a user in the Discord
        try {
            event.getGuild().getMemberById(message[1].substring(3, message[1].length() - 1)).getUser();
        } catch (StringIndexOutOfBoundsException|NullPointerException e) {
            event.getChannel().sendMessage("Invalid user. Try @ing someone").queue();
            return;
        }
        String target = event.getGuild().getMemberById(message[1].substring(3, message[1].length() - 1)).getUser().getAsTag();
        long uuid = event.getGuild().getMemberById(message[1].substring(3, message[1].length() - 1)).getUser().getIdLong();
        String tarTag = message[1];

        modifyIndCounter(counter, target, uuid, operation, amount, event, opSimp, tarTag);
    }

    public static Boolean modifyIndCounter(String counter, String username, long uuid, String operation, int amount, GuildMessageReceivedEvent event, String opSimp, String tarTag) {
        try {
            DISUSER.checkUser(uuid, username);

            // Actually update the counter integer
            PreparedStatement stmt = conn.prepareStatement("UPDATE DISUSER" +
                    " SET " + counter + " = " + counter + " " + operation + " ?" +
                    " WHERE UUID = ?;");
            stmt.setInt(1, amount);
            stmt.setLong(2, uuid);
            stmt.executeUpdate();

            // Update the username field if the user has changed their name
            PreparedStatement stmt2 = conn.prepareStatement("UPDATE DISUSER" +
                    " SET Username = ?" +
                    " WHERE UUID = ?;");
            stmt2.setString(1, username);
            stmt2.setLong(2, uuid);
            stmt2.executeUpdate();

            String countCap = counter.substring(0, 1).toUpperCase() + counter.substring(1).toLowerCase();

            logger.info("[SQL] Modified " + username + " (" + uuid + ") by: " + counter + " " + operation + " " + amount + ".");
            event.getChannel().sendMessage("**" + countCap + "** " + opSimp + " by **" + amount + "** for " + tarTag + "!").queue();
            return true;
        } catch(SQLException e) {
            logger.error("[SQL ERROR] Error modifying " + counter + " for " + username + " (" + uuid + ").", e);
            event.getChannel().sendMessage("***FAILED TO UPDATE.*** Contact bot developer.").queue();
            return false;
        }
    }

    public static void sendHelp(GuildMessageReceivedEvent event) {
        event.getChannel().sendMessage("Proper syntax: $counter  *add/sub*  *Number*  *clutches/mlgblocks*  @*User*").queue();
    }
}
