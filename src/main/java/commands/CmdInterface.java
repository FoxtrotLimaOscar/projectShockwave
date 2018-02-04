package commands;

import core.Permission;
import entities.ReactEvent;

public interface CmdInterface {
    Permission permission();
    void run(Command cmd);
    String syntax(String prefix);
    String description();
}
