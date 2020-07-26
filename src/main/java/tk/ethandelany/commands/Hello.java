package tk.ethandelany.commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Hello {

    public static void helloCommand(Member member, GuildMessageReceivedEvent event) {
        event.getChannel().sendMessage("Hey there, " + member.getEffectiveName() + "!").queue();
    }
}
