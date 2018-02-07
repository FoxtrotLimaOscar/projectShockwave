package commands;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

public class MessageLink {
    private final TextChannel CHANNEL;
    private final String MESSAGEID;
    public MessageLink(Message msg) {
        this.CHANNEL = msg.getTextChannel();
        this.MESSAGEID = msg.getId();
    }
    public MessageLink(TextChannel channel, String messageId) {
        this.CHANNEL = channel;
        this.MESSAGEID = messageId;
    }
    public Message getMessage() {
        return this.CHANNEL.getMessageById(this.MESSAGEID).complete();
    }
}
