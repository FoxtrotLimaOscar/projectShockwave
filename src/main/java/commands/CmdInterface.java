package commands;

import core.Permission;

public interface CmdInterface {
    Permission permission();
    void run(Command cmd);
    String syntax(String prefix);
    String description();
}
