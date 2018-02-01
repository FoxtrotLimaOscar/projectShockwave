package commands.music;

import commands.CmdInterface;
import commands.Command;
import commands.music.utils.PlayerManager;
import core.Permission;
import entities.GuildSets;
import entities.ReactEvent;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import settings.Database;
import tools.MsgPresets;

public class CmdPlay implements CmdInterface {

    @Override
    public Permission permission() {
        return Permission.NONE;
    }

    @Override
    public void run(Command cmd) {
        MessageReceivedEvent event = cmd.getEvent();
        Guild guild = event.getGuild();
        TextChannel tChan = event.getTextChannel();
        GuildSets guildsettings = Database.getGuildSets(guild);
        if (cmd.hasParam(1)) {
            String identifier = cmd.getRaw(1);
            if (!(identifier.startsWith("http://") || identifier.startsWith("https://"))) {
                identifier = "ytsearch:" + identifier;
            }
            PlayerManager.loadTrack(identifier, 0, false, event.getMember(), tChan);
        } else {
            tChan.sendMessage(MsgPresets.musicNoSearchfactor(guildsettings.getPrefix())).queue();
        }
    }

    @Override
    public void emoteUpdate(ReactEvent reactEvent) {

    }

    @Override
    public String syntax(String prefix) {
        return prefix + "play < Name | Link >";
    }

    @Override
    public String description() {
        return "Spielt einen Song anhand eines Links oder eines Namens ab";
    }
}
