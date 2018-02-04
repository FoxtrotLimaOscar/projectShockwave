package entities;


import net.dv8tion.jda.core.entities.TextChannel;
import core.BotConfig;

public class MusicChannel {
    private TextChannel channel;
    private boolean forced = BotConfig.getDefaultForcedMC();
    public MusicChannel(TextChannel channel) {
        this.channel = channel;
    }
    public boolean isForced() {
        return this.forced;
    }
    public void setForced(boolean forced) {
        this.forced = forced;
    }
    public TextChannel getTextChannel() {
        return this.channel;
    }
    public void setChannel(TextChannel channel) {
        this.channel = channel;
    }
}
