package tokens;

import users.User;

import java.util.Date;

public class TokenSampleImpl implements Token {
    String value;
    User user;
    Date activeUntil;

    public TokenSampleImpl(String value) {
        this.value = value;
    }

    public TokenSampleImpl() {
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
    public void setRelatedUser(User user) {
        this.user = user;
    }

    @Override
    public void setActive(Boolean active) {
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public void setActiveUntil(Date date) {
        this.activeUntil = date;
    }
}
