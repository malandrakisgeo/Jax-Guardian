package repository;

import users.Token;
import users.TokenRepo;

public class TokenRepository implements TokenRepo {
    @Override
    public Token getToken(String token) {
        return null;
    }

    @Override
    public boolean removeToken(Token token) {
        return false;
    }
}
