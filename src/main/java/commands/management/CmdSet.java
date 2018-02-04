package commands.management;

import commands.CmdInterface;
import commands.Command;
import core.Permission;
import entities.ReactEvent;

public class CmdSet implements CmdInterface {
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
}
