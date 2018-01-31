package listener;

import core.Statics;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import settings.Database;
import tools.MsgPresets;
import tools.Table;
import java.io.IOException;
import java.util.Date;

public class readyListener extends ListenerAdapter {
    public void onReady(ReadyEvent event) {
        Statics.BOT_LASTRESTART = new Date();
        printGuilds(event.getJDA());
        messageGuilds(event.getJDA());
    }

    private static void printGuilds(JDA jda) {
        try {
            String[] desc = {"Guild", "Id", "Member", "Prefix"};
            int[] sizes = {40, 18, 6, 6};
            Enum[] align = {Table.ALIGN.LEFT, Table.ALIGN.CENTER, Table.ALIGN.RIGHT, Table.ALIGN.RIGHT};
            Table table = new Table("ProjectShockwave is running on following guilds:", desc, sizes, align);
            for(Guild guild : jda.getGuilds()) {
                String[] column = {guild.getName(), guild.getId(), guild.getMembers().size() + "", Database.getGuildSets(guild).getPrefix()+ ""};
                table.addColumn(column);
            }
            Thread.sleep(500);
            System.out.println("\n" + table);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void messageGuilds(JDA jda) {
        for (Guild guild : jda.getGuilds()) {
            if(Database.getGuildSets(guild).getBootMessage()) {
                Database.getGuildSets(guild).getBotChannel().sendMessage(MsgPresets.Bootup(guild)).queue();
            }
        }
    }
}
