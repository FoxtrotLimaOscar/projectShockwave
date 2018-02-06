package commands.information;

import commands.CmdInterface;
import commands.Command;
import commands.music.utils.PlayerManager;
import commands.music.utils.TrackManager;
import core.Permission;
import core.Statics;
import core.database.Database;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.awt.*;

public class CmdDev implements CmdInterface {
    @Override
    public Permission permission() {
        return Permission.NONE;
    }

    @Override
    public void run(Command cmd) {
        cmd.getEvent().getChannel().sendMessage(getInfo(cmd.getSlice(1), cmd.getEvent().getMember())).queue();
    }

    @Override
    public String syntax(String prefix) {
        return prefix + "dev < name >";
    }

    @Override
    public String description() {
        return "Nerdy stuff";
    }


    private static MessageEmbed getInfo(String name, Member member) {
        EmbedBuilder msg = new EmbedBuilder().setColor(Color.GREEN).setTitle("\uD83D\uDCCA - " + name.toUpperCase());
        switch (name.toLowerCase()) {
            case "databaseaccesses":
                msg.setDescription(Statics.databaseAccesses + " accesses on count");
                break;
            case "queuesize":
                msg.setDescription(PlayerManager.getTrackManager(member.getGuild()).getQueue().size() + " is the queue's size");
                break;
            default:
                return new EmbedBuilder().setTitle("\uD83D\uDCCA - DEVELOPER").setDescription("There is no statistic with this name!").build();
        }
        return msg.build();
    }
}
