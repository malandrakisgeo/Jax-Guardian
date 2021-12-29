package entity;

import tokens.Token;
import users.User;

import java.util.Date;

public class TokenExample implements Token {
    String value;
    User user;
    Date activeUntil;


    public TokenExample(String value, User user, Date activeUntil) {
        this.value = value;
        this.user = user;
        this.activeUntil = activeUntil;
    }

    public TokenExample(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public User getRelatedUser() {
        return this.user;
    }

    @Override
    public Boolean isActive() {
        return this.activeUntil.after(new Date());
    }

    @Override
    public Date activeUntil() {
        return this.activeUntil;
    }

    @Override
    public void setRelatedUser(User username) {
        this.user = username;
    }

    @Override
    public void setActive(Boolean active) {
        if(active==true){

        }
    }

    @Override
    public void setValue(String value) {

    }

    @Override
    public void setActiveUntil(Date date) {
        this.activeUntil = date;
    }
}
