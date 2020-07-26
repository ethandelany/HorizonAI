package tk.ethandelany.commands;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import tk.ethandelany.tables.counters;

public class Counter {

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
        } else if(message[3].equalsIgnoreCase("mlg_blocks")) {
            counter = "mlg_blocks";
        } else {
            event.getChannel().sendMessage("Incorrect counter. Try (clutches/mlg_blocks)").queue();
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
        counterExecute(counter, target, uuid, operation, amount, event, opSimp, tarTag);
    }

    public static void counterSimple(String[] message, GuildMessageReceivedEvent event) {
        String operation = "+";
        String opSimp = "increased";
        int amount = 1;
        String counter;
        if(message[0].equalsIgnoreCase("$clutches++")) {
            counter = "clutches";
        } else if(message[0].equalsIgnoreCase("$mlg_blocks++")) {
            counter = "mlg_blocks";
        } else {
            event.getChannel().sendMessage("Incorrect counter. Try (clutches/mlg_blocks)").queue();
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

        counterExecute(counter, target, uuid, operation, amount, event, opSimp, tarTag);
    }

    private static void counterExecute(String counter, String target, long uuid, String operation, int amount, GuildMessageReceivedEvent event, String opSimp, String tarTag) {
        Boolean mod = counters.modifyIndCounter(counter, target, uuid, operation, amount);
        String countCap = counter.substring(0, 1).toUpperCase() + counter.substring(1).toLowerCase();
        if(mod) {
            event.getChannel().sendMessage("**" + countCap + "** " + opSimp + " by **" + amount + "** for " + tarTag + "!").queue();
        } else {
            event.getChannel().sendMessage("***FAILED TO UPDATE.*** Contact bot developer.").queue();
        }
    }

    public static void sendHelp(GuildMessageReceivedEvent event) {
        event.getChannel().sendMessage("Proper syntax: $counter  *add/sub*  *Number*  *clutches/mlg_blocks*  @*User*").queue();
    }
}
