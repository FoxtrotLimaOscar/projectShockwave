package commands.management;

import commands.CmdInterface;
import commands.Command;
import core.Permission;
import core.database.Database;
import core.database.groups.BSettings;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import tools.MsgPresets;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class CmdSetBot implements CmdInterface {
    @Override
    public Permission permission() {
        return Permission.BOT;
    }

    @Override
    public void run(Command cmd) {
        GuildMessageReceivedEvent event = cmd.getEvent();
        BSettings bSettings = Database.getBot();
        String prefix = Database.getGuild(event.getGuild()).getPrefix();
        MessageEmbed embed;
        if (cmd.hasParam(1)) {
            switch (cmd.getSlice(1)) {
                case "map":
                    embed = map(cmd, prefix, bSettings);
                    break;
                case "status": case "sts":
                    embed = status(cmd, prefix, bSettings);
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

    private MessageEmbed map(Command cmd, String prefix, BSettings bSettings) {
        MessageEmbed embed;
        if (!cmd.hasParam(2)) {
            embed = MsgPresets.setbotMapIOError(prefix);
        } else {
            String key = cmd.getSlice(2);
            Map<String, String> map = bSettings.getMap();
            if (key.equals("clear")) {
                map.clear();
                embed = MsgPresets.setbotMapClear();
            } else {
                if (!cmd.hasParam(3)) {
                    map.remove(key);
                    embed = MsgPresets.setbotMapRemove(key);
                } else {
                    String val = cmd.getRaw(3);
                    map.put(key, val);
                    embed = MsgPresets.setbotMapPut(key, val);
                }
            }
            bSettings.setMap(map);
        }
        return embed;
    }
    
    private static MessageEmbed status(Command cmd, String prefix, BSettings bSettings) {
        MessageEmbed embed;
        if (!cmd.hasParam(2)) {
            embed = MsgPresets.setbotStsNoParameter(prefix);
        } else {
            Game newGame = Game.playing(cmd.getRaw(2));
            bSettings.setGame(newGame);
            cmd.getEvent().getJDA().getPresence().setGame(newGame);
            embed = MsgPresets.setbotSts(newGame.getName());
        }
        return embed;
    }
}