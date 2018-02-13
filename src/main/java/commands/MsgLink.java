package commands;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

public class MsgLink {
    private final TextChannel CHANNEL;
    private final String MESSAGEID;
    public MsgLink(Message msg) {
        this.CHANNEL = msg.getTextChannel();
        this.MESSAGEID = msg.getId();
    }
    public MsgLink(TextChannel channel, String messageId) {
        this.CHANNEL = channel;
        this.MESSAGEID = messageId;
    }
    public Message getMessage() {
        return this.CHANNEL.getMessageById(this.MESSAGEID).complete();
    }
    private String getChannelId() {
        return this.CHANNEL.getId();
    }
    private String getMessageId() {
        return this.MESSAGEID;
    }
    public String getComparable() {
        return this.getChannelId() + this.getMessageId();
    }

    @Override
    public boolean equals(Object object) {
        try {
            MsgLink msgLink = (MsgLink) object;
            return msgLink.getMessageId().equals(this.getMessageId()) && msgLink.getChannelId().equals(this.getChannelId());
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override
    public String toString() {
        return "[" + this.getChannelId() + "|" + this.getMessageId() + "]";
    }
}
