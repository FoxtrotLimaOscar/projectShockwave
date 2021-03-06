package tools;

import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
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
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.Map;

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



    public static MessageEmbed logCmd(GuildMessageReceivedEvent event, boolean allowed, Permission permission) {
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


    public static MessageEmbed shutdownFinal(String reason) {
        if (reason == null) {
            return new EmbedBuilder()
                    .setColor(warnColor)
                    .setTitle("\uD83D\uDD3B - SHUTTING DOWN")
                    .setDescription("ProjectShockwave wird umgehend heruntergefahren")
                    .build();
        } else {
            return new EmbedBuilder()
                    .setColor(warnColor)
                    .setTitle("\uD83D\uDD3B - SHUTTING DOWN")
                    .setDescription("ProjectShockwave wird umgehend heruntergefahren")
                    .addField("Grund", reason, false)
                    .build();
        }
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
                .addField("Music-Channel", guilsettings.getMusicChannel().getAsMention(), true)
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


    public static MessageEmbed setuserMapPut(String key, String val) {
        return new EmbedBuilder()
                .setColor(defaultColor)
                .setTitle("⚙ - USER MAP")
                .setDescription("Dem Key [" + key + "](https://) wurde der Wert [" + val + "](https://#) zugewiesen.")
                .build();
    }
    public static MessageEmbed setuserMapRemove(String key) {
        return new EmbedBuilder()
                .setColor(defaultColor)
                .setTitle("⚙ - USER MAP")
                .setDescription("Der Key [" + key + "](https://#) wurde gelöscht.")
                .build();
    }
    public static MessageEmbed setuserMapClear() {
        return new EmbedBuilder()
                .setColor(defaultColor)
                .setTitle("⚙ - USER MAP")
                .setDescription("Die Bot-Map wurde gelöscht.")
                .build();
    }
    public static MessageEmbed setuserMapIOError(String prefix) {
        return new EmbedBuilder()
                .setColor(warnColor)
                .setTitle("⚙ - FEHLER")
                .setDescription("Falscher oder fehlender Parameter\n" + prefix + "setuser map < Schlüssel | clear > < Wert | delete >")
                .build();
    }


    public static MessageEmbed setbotNP(String prefix) {
        return new EmbedBuilder()
                .setColor(warnColor)
                .setTitle("⚙ - FEHLER")
                .setDescription("Fehlender Parameter\n" + prefix + "setguild < map | status >")
                .build();
    }
    public static MessageEmbed setbotWP(String prefix) {
        return new EmbedBuilder()
                .setColor(warnColor)
                .setTitle("⚙ - FEHLER")
                .setDescription("Falscher Parameter \n" + prefix + "setguild < map | status >")
                .build();
    }
    public static MessageEmbed setbotMapPut(String key, String val) {
        return new EmbedBuilder()
                .setColor(defaultColor)
                .setTitle("⚙ - BOT MAP")
                .setDescription("Dem Key [" + key + "](https://) wurde der Wert [" + val + "](https://#) zugewiesen.")
                .build();
    }
    public static MessageEmbed setbotMapRemove(String key) {
        return new EmbedBuilder()
                .setColor(defaultColor)
                .setTitle("⚙ - BOT MAP")
                .setDescription("Der Key [" + key + "](https://#) wurde gelöscht.")
                .build();
    }
    public static MessageEmbed setbotMapClear() {
        return new EmbedBuilder()
                .setColor(defaultColor)
                .setTitle("⚙ - BOT MAP")
                .setDescription("Die Bot-Map wurde gelöscht.")
                .build();
    }
    public static MessageEmbed setbotMapIOError(String prefix) {
        return new EmbedBuilder()
                .setColor(warnColor)
                .setTitle("⚙ - FEHLER")
                .setDescription("Falscher oder fehlender Parameter\n" + prefix + "setbot map < Schlüssel | clear > < Wert | delete >")
                .build();
    }
    public static MessageEmbed setbotSts(String sts) {
        return new EmbedBuilder()
                .setColor(defaultColor)
                .setTitle("⚙ - BOT STATUS")
                .setDescription("Der Status wurde auf [" + sts + "](https://#) gesetzt.")
                .build();
    }
    public static MessageEmbed setbotStsNoParameter(String prefix) {
        return new EmbedBuilder()
                .setColor(warnColor)
                .setTitle("⚙ - FEHLER")
                .setDescription("Fehlender Parameter\n" + prefix + "setbot status < Status >")
                .build();
    }


    public static MessageEmbed setguildNoParameter(String prefix) {
        return new EmbedBuilder()
                .setColor(warnColor)
                .setTitle("⚙ - FEHLER")
                .setDescription("Fehlender Parameter\n" + prefix + "setguild < prefix | volume | musicchannel >")
                .build();
    }
    public static MessageEmbed setguildWronParameter(String prefix) {
        return new EmbedBuilder()
                .setColor(warnColor)
                .setTitle("⚙ - FEHLER")
                .setDescription("Falscher Parameter\n" + prefix + "setguild < prefix | volume | musicchannel >")
                .build();
    }
    public static MessageEmbed setguildPfx(String prefix) {
        return new EmbedBuilder()
                .setColor(defaultColor)
                .setTitle("⚙ - GUILD PREFIX")
                .setDescription("Das neue Prefix ist \"" + prefix + "\".")
                .build();
    }
    public static MessageEmbed setguildPfxNoParameter(String prefix) {
        return new EmbedBuilder()
                .setColor(warnColor)
                .setTitle("⚙ - FEHLER")
                .setDescription("Fehlender Parameter\n" + prefix + "setguild prefix < prefix >")
                .build();
    }
    public static MessageEmbed setguildVol(int vol) {
        return new EmbedBuilder()
                .setColor(defaultColor)
                .setTitle("⚙ - GUILD VOLUME")
                .setDescription("Die neue Lautstärke ist " + vol + ".")
                .build();
    }
    public static MessageEmbed setguildVolNP(String prefix) {
        return new EmbedBuilder()
                .setColor(warnColor)
                .setTitle("⚙ - FEHLER")
                .setDescription("Fehlender Parameter\n" + prefix + "setguild volume < Lautstärke >")
                .build();
    }
    public static MessageEmbed setguildMsc(TextChannel msc) {
        return new EmbedBuilder()
                .setColor(defaultColor)
                .setTitle("⚙ - GUILD MUSICCHANNEL")
                .setDescription("Der neue MusicChannel ist " + msc.getAsMention() + ".")
                .build();
    }
    public static MessageEmbed setguildMscNP(String prefix) {
        return new EmbedBuilder()
                .setColor(warnColor)
                .setTitle("⚙ - FEHLER")
                .setDescription("Fehlender Parameter\n" + prefix + "setguild musicchannel < this | #Channel >")
                .build();
    }
    public static MessageEmbed setguildMapPut(String key, String val) {
        return new EmbedBuilder()
                .setColor(defaultColor)
                .setTitle("⚙ - GUILD MAP")
                .setDescription("Dem Key [" + key + "](https://) wurde der Wert [" + val + "](https://#) zugewiesen.")
                .build();
    }
    public static MessageEmbed setguildMapRemove(String key) {
        return new EmbedBuilder()
                .setColor(defaultColor)
                .setTitle("⚙ - GUILD MAP")
                .setDescription("Der Key [" + key + "](https://#) wurde gelöscht.")
                .build();
    }
    public static MessageEmbed setguildMapClear() {
        return new EmbedBuilder()
                .setColor(defaultColor)
                .setTitle("⚙ - GUILD MAP")
                .setDescription("Die Guild-Map wurde gelöscht.")
                .build();
    }
    public static MessageEmbed setguildMapIOError(String prefix) {
        return new EmbedBuilder()
                .setColor(warnColor)
                .setTitle("⚙ - FEHLER")
                .setDescription("Falscher oder fehlender Parameter\n" + prefix + "setguild map < Schlüssel | clear > < Wert | delete >")
                .build();
    }
    public static MessageEmbed setguildDJr(Role role) {
        String desc;
        if (role == null) {
            desc = "Es gibt nun keine DJ-Rolle mehr";
        } else {
            desc = role.getAsMention();
        }
        return new EmbedBuilder()
                .setColor(defaultColor)
                .setTitle("⚙ - GUILD DJ-ROLE")
                .setDescription(desc)
                .build();
    }
    public static MessageEmbed setguildDJrIOError(String prefix) {
        return new EmbedBuilder()
                .setColor(warnColor)
                .setTitle("⚙ - FEHLER")
                .setDescription("Inkorrekte Eingabe\n" + prefix + "setguild musicrole < @Rolle | null >")
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
    public static MessageEmbed musicQueuedInfo(String playlistTitle, String playlistLink, AudioTrackInfo trackInfo) {
        EmbedBuilder embed = new EmbedBuilder().setColor(defaultColor).setTitle("\uD83C\uDFB5 - QUEUE").setThumbnail(ProjectTools.getThumbnail(trackInfo.uri));
        if (playlistTitle == null) {
            embed.setDescription("Der Track [" + trackInfo.title + "](" + trackInfo.uri + ") wurde der Queue hinzugefügt");
        } else {
            embed.setDescription("Die Playlist [" + playlistTitle + "](" + playlistLink + ") wurde der Queue hinzugefügt");
        }
        return embed.build();
    }
    public static MessageEmbed musicDequeuedInfo(String playlistTitle, String playlistLink, AudioTrackInfo trackInfo) {
        EmbedBuilder embed = new EmbedBuilder().setColor(defaultColor).setTitle("\uD83C\uDFB5 - QUEUE").setThumbnail(ProjectTools.getThumbnail(trackInfo.uri));
        if (playlistTitle == null) {
            embed.setDescription("Der Track [" + trackInfo.title + "](" + trackInfo.uri + ") wurde von der Queue entfernt");
        } else {
            embed.setDescription("Die Playlist [" + playlistTitle + "](" + playlistLink + " wurde von der Queue entfernt");
        }
        return embed.build();
    }
    public static MessageEmbed musicPlayingInfo(String playlistTitle, String playlistLink, AudioTrackInfo trackInfo) {
        EmbedBuilder embed = new EmbedBuilder().setColor(defaultColor).setTitle("\uD83C\uDFB5 - QUEUE").setThumbnail(ProjectTools.getThumbnail(trackInfo.uri));
        if (playlistTitle == null) {
            embed.setDescription("Der Track [" + trackInfo.title + "](" + trackInfo.uri + ") wird gerade abgespielt");
        } else {
            embed.setDescription("Der Track [" + trackInfo.title + "](" + trackInfo.uri + ") aus der Playlist [" + playlistTitle + "](" + playlistLink + ") wird gerade abgespielt");
        }
        return embed.build();
    }
    public static MessageEmbed musicSearchResults(AudioPlaylist playlist) {
        EmbedBuilder builder = new EmbedBuilder().setColor(defaultColor).setTitle("\uD83C\uDFB5 - SUCHERGEBNISSE");
        for (int loop = 1; loop <= 5; loop ++) {
            AudioTrackInfo info = playlist.getTracks().get(loop-1).getInfo();
            builder.addField("Suchergebnis " + loop, "[" + info.title + "](" + info.uri + ")", false);
        }
        return builder.build();
    }
    public static MessageEmbed musicPaused() {
        return new EmbedBuilder()
                .setColor(defaultColor)
                .setTitle("\uD83C\uDFB5 - MUSIC")
                .setDescription("Die Wiedergabe wurde pausiert")
                .build();
    }
    public static MessageEmbed musicResumed() {
        return new EmbedBuilder()
                .setColor(defaultColor)
                .setTitle("\uD83C\uDFB5 - MUSIC")
                .setDescription("Die Wiedergabe wurde fortgesetzt")
                .build();
    }
    public static MessageEmbed musicNotConnected() {
        return new EmbedBuilder()
                .setColor(defaultColor)
                .setTitle("⚠ - MUSIC")
                .setDescription("Du musst in einem VoiceChannel sein um Musik abzuspielen")
                .build();
    }
    public static MessageEmbed musicStopped() {
        return new EmbedBuilder()
                .setColor(defaultColor)
                .setTitle("\uD83C\uDFB5 - MUSIC")
                .setDescription("Die Wiedergabe wurde gestoppt")
                .build();
    }
    public static MessageEmbed musicWrongTextChannel(TextChannel channel) {
        return new EmbedBuilder()
                .setColor(defaultColor)
                .setTitle("⚠ - MUSIC")
                .setDescription("Alle Infos zur Queue landen in " + channel.getAsMention())
                .build();
    }
}
