package tokens;


import users.User;

public interface TokenRepo {

    Token createToken(Token token);
    Token getToken(String token);
    boolean removeToken(Token token);
    boolean removeToken(String tokenId);

    boolean setTokenAsActive(Token token);
    Token getTokenForUser(User user);

}
