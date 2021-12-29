package tokens;

import users.User;

import java.util.Date;

public interface Token {

    String getValue();
    User getRelatedUser();
    Boolean isActive();
    Date activeUntil();

    void setRelatedUser(User username);
    void setActive(Boolean active);
    void setValue(String value);
    void setActiveUntil(Date date);
}
