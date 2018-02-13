package commands.management;

import commands.CmdInterface;
import commands.Command;
import core.Permission;

public class CmdSetBot implements CmdInterface {
    @Override
    public Permission permission() {
        return null;
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
        return null;
    }

    @Override
    public String details() {
        return null;
    }
}
