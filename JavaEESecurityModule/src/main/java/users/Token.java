package users;

public interface Token {
    String getValue();
    String getRelatedUser();
    Boolean isActive();
}
