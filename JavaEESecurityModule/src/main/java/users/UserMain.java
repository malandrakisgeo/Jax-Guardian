package users;

import java.util.Set;

public class UserMain implements User{
    @Override
    public String getUsername() {
        return "GEORGE";
    }

    @Override
    public Set<String> getUserRoles() {
        return null;
    }
}
