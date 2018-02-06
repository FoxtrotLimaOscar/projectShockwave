package listener;

import commands.CmdHandler;
import commands.ReactEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class reactListener extends ListenerAdapter {
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        CmdHandler.fireReactionTicket(new ReactEvent(event));
    }

    public void onMessageReactionRemove(MessageReactionRemoveEvent event) {
        CmdHandler.fireReactionTicket(new ReactEvent(event));
    }
}
