package core;

import entities.DJRole;
import net.dv8tion.jda.core.entities.Member;
import settings.BotConfig;
import core.database.Database;

import java.util.Arrays;

public enum Permission {
    BOT,
    GUILD,
    DJ,
    NONE;

    public static boolean hasPermission(Member member, Enum permission) {
        switch (permission.toString()) {
            case "BOT":
                if (Arrays.asList(BotConfig.getBotOperatorIds()).contains(member.getUser().getId())){
                    return true;
                }
                break;
            case "GUILD":
                if(member.hasPermission(net.dv8tion.jda.core.Permission.ADMINISTRATOR)) {
                    return true;
                }
                break;
            case "DJ":
                DJRole djr =Database.getGuild(member.getGuild()).getDJRole();
                if (!djr.isForced() || member.getRoles().contains(djr.getRole())) {
                    return true;
                }
                break;
            case "NONE":
                return true;
        }
        return false;
    }


    public static String getSymbol(Enum premission) {
        switch (premission.toString()) {
            case "BOT":
                return Statics.SYMBOL_PERMISSION_BOT;
            case "ADMIN":
                return Statics.SYMBOL_PERMISSION_ADMIN;
            case "DJ":
                return Statics.SYMBOL_PERMISSION_DJ;
            default:
                return Statics.SYMBOL_PERMISSION_NONE;
        }
    }
}
