package commands.management;

import commands.CmdInterface;
import commands.Command;
import commands.music.utils.PlayerManager;
import core.Permission;
import core.database.Database;
import core.database.groups.GSettings;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import tools.SubsToolkit;

public class CmdSetGuild implements CmdInterface {
    @Override
    public Permission permission() {
        return Permission.GUILD;
    }

    @Override
    public void run(Command cmd) {
        GuildMessageReceivedEvent event = cmd.getEvent();
        Guild guild = event.getGuild();
        GSettings gSettings = Database.getGuild(guild);
        if (!cmd.hasParam(1)) return;
        switch (cmd.getSlice(1)) {
            case "prefix": case "pfx":
                if (cmd.hasParam(2)) {
                    gSettings.setPrefix(SubsToolkit.limitString(cmd.getSlice(2), 5));
                }
                break;
            case "volume": case "vol":
                if (cmd.hasParam(2)) {
                    int vol = Integer.parseInt(cmd.getSlice(2));
                    gSettings.setVolume(vol);
                    PlayerManager.getTrackManager(guild).setVolume(vol);
                }
                break;
            case "musicchannel": case "msc":
                if (cmd.hasParam(2)) {
                    TextChannel msc = event.getMessage().getMentionedChannels().get(0);
                    gSettings.setMusicChannel(gSettings.getMusicChannel().setTextChannel(msc));
                }
                break;
        }
    }

    @Override
    public String syntax(String prefix) {
        return null;
    }

    @Override
    public String description() {
        return null;
    }

    @Override
    public String details() {
        return null;
    }
}
