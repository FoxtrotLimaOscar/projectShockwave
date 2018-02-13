package core.database.structures;


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
    public MusicChannel setForced(boolean forced) {
        this.forced = forced;
        return this;
    }
    public TextChannel getTextChannel() {
        return this.channel;
    }
    public MusicChannel setTextChannel(TextChannel channel) {
        this.channel = channel;
        return this;
    }
}
