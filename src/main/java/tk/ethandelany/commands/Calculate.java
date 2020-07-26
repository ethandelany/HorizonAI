package tk.ethandelany.commands;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Calculate {

    public static void calculateCommand(String[] message, GuildMessageReceivedEvent event) {

        int num1 = Integer.parseInt(message[2]);
        int num2 = Integer.parseInt(message[3]);

        switch (message[1]) {
            case "plus":
                event.getChannel().sendMessage("Answer: " + (num1 + num2)).queue();
                break;
            case "minus":
                event.getChannel().sendMessage("Answer: " + (num1 - num2)).queue();
                break;
        }
    }
}
