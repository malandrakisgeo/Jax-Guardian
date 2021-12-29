package repository;

import entity.TokenExample;
import tokens.Token;
import tokens.TokenRepo;
import users.User;

import javax.inject.Singleton;
import java.util.HashMap;

@Singleton
public class TokenRepository implements TokenRepo {
    HashMap<String, Token> hashMapInsteadOfDb = new HashMap<String, Token>();

    @Override
    public Token createToken(Token token) {
        Token tok = new TokenExample(token.getValue(), token.getRelatedUser(), token.activeUntil());
        hashMapInsteadOfDb.put(tok.getValue(), tok);
        return tok;
    }

    @Override
    public Token getToken(String token) {
        return this.hashMapInsteadOfDb.get(token);
    }

    @Override
    public boolean removeToken(Token token) {
        return this.removeToken(token.getValue());
    }

    @Override
    public boolean removeToken(String tokenId) {
        this.hashMapInsteadOfDb.remove(tokenId);
        return  true;
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
