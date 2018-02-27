package commands.management;

import commands.CmdInterface;
import commands.Command;
import commands.music.utils.PlayerManager;
import core.Permission;
import core.database.Database;
import core.database.groups.GSettings;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import tools.MsgPresets;
import tools.SubsToolkit;

import java.util.Map;
import java.util.concurrent.TimeUnit;

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
        String prefix = gSettings.getPrefix();
        MessageEmbed embed;
        if (cmd.hasParam(1)) {
            switch (cmd.getSlice(1)) {
                case "map":
                    embed = map(cmd, prefix, gSettings);
                    break;
                case "prefix": case "pfx":
                    embed = pfx(cmd, prefix, gSettings);
                    break;
                case "volume": case "vol":
                    embed = vol(cmd, prefix, gSettings, guild);
                    break;
                case "musicchannel": case "msc":
                    embed = msc(cmd, prefix, gSettings, event);
                    break;
                case "djrole": case "djr":
                    embed = msr(cmd, prefix, gSettings, event);
                    break;
                default:
                    embed = MsgPresets.setguildWronParameter(prefix);
                    break;
            }
        } else {
            embed = MsgPresets.setguildNoParameter(prefix);
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

    private MessageEmbed map(Command cmd, String prefix, GSettings gSettings) {
        MessageEmbed embed;
        if (!cmd.hasParam(2)) {
            embed = MsgPresets.setguildMapIOError(prefix);
        } else {
            String key = cmd.getSlice(2);
            Map<String, String> map = gSettings.getMap();
            if (key.equals("clear")) {
                map.clear();
                embed = MsgPresets.setguildMapClear();
            } else {
                if (!cmd.hasParam(3)) {
                    map.remove(key);
                    embed = MsgPresets.setguildMapRemove(key);
                } else {
                    String val = cmd.getRaw(3);
                    map.put(key, val);
                    embed = MsgPresets.setguildMapPut(key, val);
                }
            }
            gSettings.setMap(map);
        }
        return embed;
    }
    private MessageEmbed pfx(Command cmd, String prefix, GSettings gSettings) {
        MessageEmbed embed;
        if (cmd.hasParam(2)) {
            gSettings.setPrefix(SubsToolkit.limitString(cmd.getSlice(2), 5));
            embed = MsgPresets.setguildPfx(gSettings.getPrefix());
        } else {
            embed = MsgPresets.setguildPfxNoParameter(prefix);
        }
        return embed;
    }

    private static MessageEmbed vol(Command cmd, String prefix, GSettings gSettings, Guild guild) {
        MessageEmbed embed;
        if (cmd.hasParam(2)) {
            int vol = Integer.parseInt(cmd.getSlice(2));
            gSettings.setVolume(vol);
            PlayerManager.getTrackManager(guild).setVolume(vol);
            embed = MsgPresets.setguildVol(gSettings.getVolume());
        } else {
            embed = MsgPresets.setguildVolNP(prefix);
        }
        return embed;
    }

    private static MessageEmbed msc(Command cmd, String prefix, GSettings gSettings, GuildMessageReceivedEvent event) {
        MessageEmbed embed;
        if (cmd.hasParam(2)) {
            TextChannel msc;
            if (cmd.getSlice(2).equals("this")) {
                msc = event.getChannel();
            } else {
                msc = event.getMessage().getMentionedChannels().get(0);
            }
            gSettings.setMusicChannel(msc);
            embed = MsgPresets.setguildMsc(gSettings.getMusicChannel());
        } else {
            embed = MsgPresets.setguildMscNP(prefix);
        }
        return embed;
    }

    private static MessageEmbed msr(Command cmd, String prefix, GSettings gSettings, GuildMessageReceivedEvent event) {
        MessageEmbed embed;
        if (cmd.hasParam(2)) {
            Role djr;
            try {
                djr = event.getMessage().getMentionedRoles().get(0);
            } catch (Exception e) {
                djr = null;
            }
            gSettings.setDJRole(djr);
            embed = MsgPresets.setguildDJr(djr);
        } else {
            embed = MsgPresets.setguildDJrIOError(prefix);
        }
        return embed;
    }
}
