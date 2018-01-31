package commands.music;

import commands.CmdInterface;
import commands.Command;
import core.Permission;
import entities.ReactEvent;

public class cmdPlay implements CmdInterface {

    @Override
    public Permission permission() {
        return Permission.NONE;
    }

    @Override
    public void run(Command cmd) {

    }

    @Override
    public void emoteUpdate(ReactEvent reactEvent) {

    }

    @Override
    public String syntax(String prefix) {
        return prefix + "play";
    }

    @Override
    public String description() {
        return "Spielt einen Song ab";
    }
}
