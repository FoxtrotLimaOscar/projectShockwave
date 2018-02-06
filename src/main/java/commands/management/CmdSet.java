package commands.management;

import commands.CmdInterface;
import commands.Command;
import core.Permission;

public class CmdSet implements CmdInterface {
    @Override
    public Permission permission() {
        return Permission.NONE;
    }

    @Override
    public void run(Command cmd) {

    }

    @Override
    public String syntax(String prefix) {
        return null;
    }

    @Override
    public String description() {
        return "Lässt ";
    }
}
