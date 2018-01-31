package commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import settings.Database;

public class Command {

    private MessageReceivedEvent event;
    private String raw;
    private String beheaded;
    private String[] sliced;

    public Command(MessageReceivedEvent event) {
        this.event = event;
        this.raw = this.event.getMessage().getContentRaw();
        this.beheaded = this.raw.replaceFirst(Database.getGuildSets(event.getGuild()).getPrefix() + "", "");
        this.sliced = this.beheaded.split(" ");
    }

    public String getRaw() {
        return this.raw;
    }
    public String getBeheaded() {
        return this.beheaded;
    }
    public String getSlice(int i) {
        return sliced[i];
    }
    public String getInvoke() {
        return sliced[0];
    }
    public String[] getSlices() {
        return this.sliced;
    }
    public MessageReceivedEvent getEvent() {
        return this.event;
    }
}
