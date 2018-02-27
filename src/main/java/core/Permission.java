package core;

import net.dv8tion.jda.core.entities.Member;
import core.database.Database;
import net.dv8tion.jda.core.entities.Role;

public enum Permission {
    BOT,
    GUILD,
    DJ,
    NONE;

    public static boolean hasPermission(Member member, Enum permission) {
        switch (permission.toString()) {
            case "BOT":
                if (BotConfig.getBotownerIds().contains(member.getUser().getId())) {
                    return true;
                }
                break;
            case "GUILD":
                if(member.hasPermission(net.dv8tion.jda.core.Permission.ADMINISTRATOR)) {
                    return true;
                }
                break;
            case "DJ":
                Role djr =Database.getGuild(member.getGuild()).getDJRole();
                if (djr == null || member.getRoles().contains(djr)) {
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
