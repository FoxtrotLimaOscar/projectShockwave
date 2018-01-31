package listener;

import commands.CmdHandler;
import commands.Command;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import settings.Database;

public class msgListener extends ListenerAdapter {
    public void onMessageReceived(MessageReceivedEvent event) {
        String prefix = Database.getGuildSets(event.getGuild()).getPrefix();
        if (event.getChannelType().isGuild() && !event.getAuthor().isBot() && event.getMessage().getContentRaw().startsWith(prefix) && event.getChannelType().isGuild()) {
            CmdHandler.handleCommand(new Command(event));
        }
    }
}
