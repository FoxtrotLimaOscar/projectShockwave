package commands;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionRemoveEvent;

public class ReactEvent {
    private MsgLink msgLink;
    private String emote;
    private User user;
    public ReactEvent(MessageReactionAddEvent event) {
        this.msgLink = new MsgLink(event.getTextChannel(), event.getMessageId());
        this.emote = event.getReactionEmote().getName();
        this.user = event.getUser();
    }
    public ReactEvent(MessageReactionRemoveEvent event) {
        this.msgLink = new MsgLink(event.getTextChannel(), event.getMessageId());
        this.emote = event.getReactionEmote().getName();
        this.user = event.getUser();
    }

    public String getMessageId() {
        return msgLink.getMessage().getId();
    }

    public MsgLink getMessageLink() {
        return this.msgLink;
    }

    public Message getMessage() {
        return msgLink.getMessage();
    }
    public String getEmote() {
        return emote;
    }
    public User getUser() {
        return user;
    }
}
