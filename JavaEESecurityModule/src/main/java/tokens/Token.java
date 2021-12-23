package tokens;

public interface Token {
    String getValue();
    String getRelatedUser();
    Boolean isActive();

    void setRelatedUser(String username);
    void setActive(Boolean active);
    void setValue(String value);
}
