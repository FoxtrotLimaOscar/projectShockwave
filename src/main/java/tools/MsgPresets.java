package tools;

import commands.CmdHandler;
import core.Permission;
import core.Statics;
import core.database.groups.BSettings;
import core.database.groups.GSettings;
import core.database.groups.USettings;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import core.database.Database;

import java.awt.*;
public class MsgPresets extends EmbedBuilder {

    private static Color warnColor = Color.orange;
    private static Color defaultColor = Color.blue;


    public static MessageEmbed Bootup(Guild guild) {
        return new EmbedBuilder()
                .setColor(Color.orange)
                .setTitle("\uD83D\uDD3A - BOOTED UP")
                .setDescription("ProjectShockwave / " + guild.getSelfMember().getEffectiveName() + " ist nun Betriebsbereit!")
                .build();
    }



    public static MessageEmbed logCmd(MessageReceivedEvent event, boolean allowed, Permission permission) {
        String allowedText;
        if (allowed) {
            allowedText = "permission granted";
        } else {
            allowedText = "permission rejected";
        }
        Color color = null;
        switch (permission.toString()) {
            case "DJ":
                color = Statics.COLOR_PERMISSION_DJ;
                break;
            case "ADMIN":
                color = Statics.COLOR_PERMISSION_ADMIN;
                break;
            case "BOT":
                color = Statics.COLOR_PERMISSION_BOT;
                break;
            default:
                color = Statics.COLOR_PERMISSION_NONE;
                break;
        }
        return new EmbedBuilder()
                .setColor(color)
                .setTitle(event.getMessage().getContentDisplay())
                .addField("User", event.getAuthor().getAsMention(),true)
                .addField("Guild", event.getGuild().getName(), true)
                .setFooter(allowedText, null)
                .build();
    }



    public static MessageEmbed noPermission(Permission permission, Member member) {
        String description = "";
        for (Member index : member.getGuild().getMembers()){
            if (Permission.hasPermission(index, permission)) {
                description += ", " + index.getAsMention();
            }
        }
        if (description.equals("")) {
            description = "Niemand auf dieser Guild";
        } else {
            description = description.substring(2);
        }
        return new EmbedBuilder()
                .setColor(Color.orange)
                .setTitle("RESTRICTED")
                .setDescription("Du hast leider nicht die Rechte um diesen Befehl auszuführen.")
                .addField("User mit diesem Recht", description, false)
                .build();
    }

    public static MessageEmbed Ping(JDA jda) {
        return new EmbedBuilder()
                .setColor(defaultColor)
                .setTitle("⏲ - PING")
                .setDescription("ProjectShockwave hat Momentan eine Latenzzeit von " + jda.getPing() + "ms.")
                .build();
    }


    public static MessageEmbed ShutdownAccept() {
        return new EmbedBuilder()
                .setColor(warnColor)
                .setTitle("♦ - SHUTDOWN")
                .setDescription("Bist du dir sicher dass du ProjectShockwave herunterfahren möchtest?")
                .build();
    }


    public static MessageEmbed Shutdown() {
        return new EmbedBuilder()
                .setColor(warnColor)
                .setTitle("\uD83D\uDD3B - SHUTTING DOWN")
                .setDescription("ProjectShockwave wird umgehend heruntergefahren")
                .build();
    }


    public static MessageEmbed helpNoSuchCmd() {
        return new EmbedBuilder()
                .setColor(warnColor)
                .setTitle("❓ - BEFEHL EXISTIERT NICHT")
                .setDescription("Der Befehl für den du dich interessierst existiert nicht, möglicherweise hast du dich vertippt")
                .build();
    }


    public static MessageEmbed helpAll() {
        EmbedBuilder msg = new EmbedBuilder();
        msg.setColor(defaultColor).setTitle("\uD83D\uDDC2 - ALLE BEFEHLE");
        CmdHandler.commands.forEach((name, command) -> {
            msg.addField(name, command.description(), true);
        });
        return msg.build();
    }


    public static MessageEmbed sendHelp() {
        return new EmbedBuilder()
                .setColor(defaultColor)
                .setTitle("ℹ - ALLE BEFEHLE")
                .setDescription("Du hast privat eine Nachricht mit allen Befehlen erhalten!")
                .build();
    }


    public static MessageEmbed cmdHelp(String cmd, String description, Enum permission, String syntax) {
        return new EmbedBuilder()
                .setColor(defaultColor)
                .setTitle("ℹ - " + cmd.toUpperCase())
                .setDescription(description)
                .addField("Permission", Permission.getSymbol(permission) + " - " + permission.toString().toUpperCase(), true)
                .addField("Anwendung", syntax, true)
                .build();
    }


    public static MessageEmbed settingsPageFront() {
        String userInfo = "-Saves";
        String guildInfo = "-Prefix\n-Boot-Nachrichten senden\n-Bot-Channel\n-Music-Channel\n-Erzwungener Music-Channel\n-DJRolle";
        String botInfo = "-Status";
        return new EmbedBuilder()
                .setColor(defaultColor)
                .setTitle("⚙ - ALLE EINSTELLUNGEN")
                .addField("User-Einstellungen", userInfo, true)
                .addField("Guild-Einstellungen", guildInfo, true)
                .addField("Bot-Einstellungen", botInfo, true)
                .setFooter("Seite 1/4", null)
                .build();
    }
    public static MessageEmbed settingsPageUser(User user) {
        USettings usersettings = Database.getUser(user);
        return new EmbedBuilder()
                .setColor(defaultColor)
                .setTitle("⚙ - USER EINSTELLUNGEN")
                .addField("Saves", "TODO", true)
                .setFooter("Seite 2/4", null)
                .build();
    }
    public static MessageEmbed settingsPageGuild(Guild guild) {
        GSettings guilsettings = Database.getGuild(guild);
        return new EmbedBuilder()
                .setColor(defaultColor)
                .setTitle("⚙ - GUILD EINSTELLUNGEN")
                .addField("Prefix", guilsettings.getPrefix(), true)
                .addField("Boot-Nachrichten senden", SubsToolkit.boolSymbol(guilsettings.getBootMessage()), true)
                .addField("Bot-Channel", guilsettings.getBotChannel().getAsMention(), true)
                .addField("Music-Channel", guilsettings.getMusicChannel().getTextChannel().getAsMention(), true)
                .addField("Erzwungener Music-Channel", SubsToolkit.boolSymbol(guilsettings.getMusicChannel().isForced()), true)
                .addField("", "", true)
                .setFooter("Seite 3/4", null)
                //.addField("DJRolle", guilsettings.getDJRole().getRole().getAsMention(), true)
                .build();
    }
    public static MessageEmbed settingsPageBot() {
        BSettings botsettings = Database.getBot();
        return new EmbedBuilder()
                .setColor(defaultColor)
                .setTitle("⚙ - BOT EINSTELLUNGEN")
                .addField("Status", botsettings.getGame().getName(), true)
                .setFooter("Seite 4/4", null)
                .build();
    }
    public static MessageEmbed musicNoSearchfactor(String prefix) {
        return new EmbedBuilder()
                .setColor(defaultColor)
                .setTitle("\uD83C\uDFB5 - KEIN PARAMETER")
                .setDescription("Um einen Song abzuspielen musst du entweder einenen Link oder einen Name eingeben.\nGenaueres findest du unter " + prefix + "help play")
                .build();
    }
    public static MessageEmbed musicNoResultsFound() {
        return new EmbedBuilder()
                .setColor(defaultColor)
                .setTitle("\uD83C\uDFB5 - KEINE ERGEBNISSE")
                .setDescription("Es wurden keine Songs gefunden")
                .build();
    }
    public static MessageEmbed musicQueuedInfo(boolean playlist, String title, String uri) {
        String description;
        if (playlist) {
            description = "Die Playlist \"" + title + "\" wurde der Queue hinzugefügt";
        } else {
            description = "Der Track \"" + title + "\" wurde der Queue hinzugefügt";
        }
        uri = ProjectTools.getThumbnail(uri);
        return new EmbedBuilder()
                .setColor(defaultColor)
                .setTitle("\uD83C\uDFB5 - QUEUE")
                .setDescription(description)
                .setThumbnail(uri)
                .build();
    }
    public static MessageEmbed musicPlayingInfo(String tile, String uri) {
        uri = ProjectTools.getThumbnail(uri);
        return new EmbedBuilder()
                .setColor(defaultColor)
                .setTitle("\uD83C\uDFB5 - QUEUE")
                .setDescription("Der Track \"" + tile + "\" wird nun abgespielt")
                .setThumbnail(uri)
                .build();
    }
}
