package listener;

import commands.CmdHandler;
import commands.Command;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import core.database.Database;

public class msgListener extends ListenerAdapter {
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String prefix = Database.getGuild(event.getGuild()).getPrefix();
        if (event.getChannelType().isGuild() && !event.getAuthor().isBot() && event.getMessage().getContentRaw().startsWith(prefix) && event.getChannelType().isGuild()) {
            CmdHandler.handleCommand(new Command(event));
        }
    }
}
