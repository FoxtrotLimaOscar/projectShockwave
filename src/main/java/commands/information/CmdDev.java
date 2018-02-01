package commands.information;

import commands.CmdInterface;
import commands.Command;
import core.Permission;
import core.Statics;
import entities.ReactEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.awt.*;

public class CmdDev implements CmdInterface {
    @Override
    public Permission permission() {
        return Permission.NONE;
    }

    @Override
    public void run(Command cmd) {
        cmd.getEvent().getTextChannel().sendMessage(getInfo(cmd.getSlice(1))).queue();
    }

    @Override
    public void emoteUpdate(ReactEvent reactEvent) {

    }

    @Override
    public String syntax(String prefix) {
        return prefix + "dev < name >";
    }

    @Override
    public String description() {
        return "Nerdy stuff";
    }


    private static MessageEmbed getInfo(String name) {
        EmbedBuilder msg = new EmbedBuilder().setColor(Color.GREEN).setTitle("\uD83D\uDCCA - " + name.toUpperCase());
        switch (name.toLowerCase()) {
            case "databaseaccesses":
            case "dbacs":
            case "dac":
                msg.setDescription(Statics.databaseAccesses + " accesses on count");
                break;
            default:
                return new EmbedBuilder().setTitle("\uD83D\uDCCA - DEVELOPER").setDescription("There is no statistic with this name!").build();
        }
        return msg.build();
    }
}
