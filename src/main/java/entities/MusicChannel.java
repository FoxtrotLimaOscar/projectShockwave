package entities;


import net.dv8tion.jda.core.entities.Channel;
import settings.BotConfig;

public class MusicChannel {
    private Channel channel;
    private boolean forced = BotConfig.getDefaultForcedMC();
    public MusicChannel(Channel channel) {
        this.channel = channel;
    }
    public boolean isForced() {
        return this.forced;
    }
    public void setForced(boolean forced) {
        this.forced = forced;
    }
    public Channel getChannel() {
        return this.channel;
    }
    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
