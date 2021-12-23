package repository;

import tokens.Token;
import tokens.TokenRepo;
import users.User;

public class TokenRepository implements TokenRepo {
    @Override
    public Token getToken(String token) {
        return null;
    }

    @Override
    public boolean removeToken(Token token) {
        return false;
    }

    @Override
    public boolean removeToken(String tokenId) {
        return false;
    }

    @Override
    public boolean setTokenAsActive(Token token) {
        return false;
    }

    @Override
    public Token getTokenForUser(User user) {
        return null;
    }
}
