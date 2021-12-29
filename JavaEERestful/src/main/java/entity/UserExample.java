package entity;

import users.User;

import java.util.Set;

public class UserExample implements User {

    private String username;
    private Set<String> roles;

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public void setUserRoles(Set<String> roles) {

    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public Set<String> getUserRoles() {
        return this.roles;
    }
}
