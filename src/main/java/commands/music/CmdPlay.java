package commands.music;

import commands.CmdInterface;
import commands.Command;
import commands.ReactHandler;
import commands.music.utils.PlayerManager;
import core.Permission;
import core.database.groups.GSettings;
import entities.ReactEvent;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import core.database.Database;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import tools.MsgPresets;

public class CmdPlay implements CmdInterface, ReactHandler {

    private Guild guild;
    private User user;

    @Override
    public Permission permission() {
        return Permission.NONE;
    }

    @Override
    public void run(Command cmd) {
        GuildMessageReceivedEvent event = cmd.getEvent();
        this.user = event.getAuthor();
        this.guild = event.getGuild();
        TextChannel tChan = event.getChannel();
        GSettings guildsettings = Database.getGuild(this.guild);
        if (cmd.hasParam(1)) {
            String identifier = cmd.getRaw(1);
            if (!(identifier.startsWith("http://") || identifier.startsWith("https://"))) {
                identifier = "ytsearch:" + identifier;
            }
            PlayerManager.loadTrack(identifier, 0, false, event.getMember(), tChan, this);
        } else {
            tChan.sendMessage(MsgPresets.musicNoSearchfactor(guildsettings.getPrefix())).queue();
        }
    }

    @Override
    public void emoteUpdate(ReactEvent reactEvent) {
        boolean sameUser = this.user.equals(reactEvent.getUser());
        if (sameUser && reactEvent.getEmote().equals("❌")) {
            reactEvent.getMessage().getTextChannel().sendMessage("COMING SOON").queue();
        }
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
