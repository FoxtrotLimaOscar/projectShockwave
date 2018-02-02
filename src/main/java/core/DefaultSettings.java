package core;

import net.dv8tion.jda.core.entities.User;

public class DefaultSettings {
    private String[] BOTOPERATORIDS = new String[] {"265955256439930882", "289423581077831681"};
    public static final int YTPLAYLISTLIMIT = 100;
    public static final String DEFAULT_PREFIX = "#";
    public static final boolean DEFAULT_FORCEDMUSICCHANNEL = false;
    public static final int DEFAULT_VOLUME = 100;

    public boolean isBotOperator(User user) {
        for(String botOperatorId : BOTOPERATORIDS) {
            if (user.getId().equals(botOperatorId)) return true;
        }
        return false;
    }
}
