package tk.ethandelany.helpers;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import tk.ethandelany.commands.Counter;
import tk.ethandelany.commands.Hello;
import tk.ethandelany.commands.Calculate;

public class CommandHelper extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] message = event.getMessage().getContentRaw().toLowerCase().split(" ");
        Member member = event.getMember();

        if(message[0].length() < 1) {
            return;
        }

        if(message[0].charAt(0) == '$') {
            switch(message[0]) {
                case "$calculate":
                    Calculate.calculateCommand(message, event);
                    break;
                case "$counter":
                    if(message.length == 5) {
                        Counter.counterStandard(message, event);
                    } else {
                        event.getChannel().sendMessage("Incorrect amount of arguments.").queue();
                        Counter.sendHelp(event);
                    }
                    break;
                case "$clutches++":
                case "$mlg_blocks++":
                    if(message.length == 2) {
                        Counter.counterSimple(message, event);
                    } else {
                        event.getChannel().sendMessage("Incorrect amount of arguments.").queue();
                        Counter.sendHelp(event);
                    }
                    break;
                default:
                    event.getChannel().sendMessage("I'm sorry, I don't recognize that command!").queue();
            }
        } else {
            switch(message[0]) {
                case "hello":
                    Hello.helloCommand(member, event);
                    break;
            }
        }
    }
}
