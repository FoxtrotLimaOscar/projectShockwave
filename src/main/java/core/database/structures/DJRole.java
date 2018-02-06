package core.database.structures;

import net.dv8tion.jda.core.entities.Role;

public class DJRole {
    private Role role = null;
    private boolean forced = true;

    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }
    public boolean isForced() {
        return role != null && forced;
    }
    public void setForced(boolean forced) {
        this.forced = forced;
    }
}
