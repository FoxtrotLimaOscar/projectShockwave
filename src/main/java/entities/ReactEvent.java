package entities;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionRemoveEvent;

public class ReactEvent {
    private String messageID;
    private TextChannel messageChannel;
    private String emote;
    private User user;
    public ReactEvent(MessageReactionAddEvent event) {
        this.messageID = event.getMessageId();
        this.messageChannel = event.getTextChannel();
        this.emote = event.getReactionEmote().getName();
        this.user = event.getUser();
    }
    public ReactEvent(MessageReactionRemoveEvent event) {
        this.messageID = event.getMessageId();
        this.messageChannel = event.getTextChannel();
        this.emote = event.getReactionEmote().getName();
        this.user = event.getUser();
    }

    public String getMessageID() {
        return messageID;
    }

    public Message getMessage() {
        return messageChannel.getMessageById(messageID).complete();
    }
    public String getEmote() {
        return emote;
    }
    public User getUser() {
        return user;
    }
}
