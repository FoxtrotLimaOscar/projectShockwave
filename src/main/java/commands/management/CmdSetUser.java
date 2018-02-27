package commands.management;

import commands.CmdInterface;
import commands.Command;
import core.Permission;
import core.database.Database;
import core.database.groups.GSettings;
import core.database.groups.USettings;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import tools.MsgPresets;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class CmdSetUser implements CmdInterface {
    @Override
    public Permission permission() {
        return Permission.NONE;
    }

    @Override
    public void run(Command cmd) {
        GuildMessageReceivedEvent event = cmd.getEvent();
        User user = event.getAuthor();
        USettings uSettings = Database.getUser(user);
        String prefix = Database.getGuild(event.getGuild()).getPrefix();
        MessageEmbed embed;
        if (cmd.hasParam(1)) {
            switch (cmd.getSlice(1)) {
                case "map":
                    embed = map(cmd, prefix, uSettings);
                    break;
                default:
                    embed = MsgPresets.setbotWP(prefix);
                    break;
            }
        } else {
            embed = MsgPresets.setbotNP(prefix);
        }
        event.getChannel().sendMessage(embed).queue(m -> m.delete().queueAfter(10, TimeUnit.SECONDS));
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

    private MessageEmbed map(Command cmd, String prefix, USettings uSettings) {
        MessageEmbed embed;
        if (!cmd.hasParam(2)) {
            embed = MsgPresets.setuserMapIOError(prefix);
        } else {
            String key = cmd.getSlice(2);
            Map<String, String> map = uSettings.getMap();
            if (key.equals("clear")) {
                map.clear();
                embed = MsgPresets.setuserMapClear();
            } else {
                if (!cmd.hasParam(3)) {
                    map.remove(key);
                    embed = MsgPresets.setuserMapRemove(key);
                } else {
                    String val = cmd.getRaw(3);
                    map.put(key, val);
                    embed = MsgPresets.setuserMapPut(key, val);
                }
            }
            uSettings.setMap(map);
        }
        return embed;
    }
}
