package users;


public interface TokenRepo {

    Token getToken(String token);
    boolean removeToken(Token token);

}
