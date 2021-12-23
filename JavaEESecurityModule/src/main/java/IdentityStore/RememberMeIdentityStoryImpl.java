package IdentityStore;

import tokens.Token;
import tokens.TokenGeneratorService;
import tokens.TokenRepo;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.CallerPrincipal;
import javax.security.enterprise.authentication.mechanism.http.RememberMe;
import javax.security.enterprise.credential.RememberMeCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.RememberMeIdentityStore;
import java.util.Set;

@ApplicationScoped
@FirstImplementation
@RememberMe
public class RememberMeIdentityStoryImpl implements RememberMeIdentityStore {

    @Inject
    TokenGeneratorService tokenGen;

    @Inject
    TokenRepo tokenRepo;

    @Override
    public CredentialValidationResult validate(RememberMeCredential credential) {
        Token token = tokenRepo.getToken(credential.getToken());
        if(token.isActive()){ //If the token is currently active in another session
            tokenRepo.removeToken(token); //it is regarded as compromised and removed
            return null;
        }
    return null;
    }

    @Override
    public String generateLoginToken(CallerPrincipal callerPrincipal, Set<String> set) {
        return this.tokenGen.token(callerPrincipal.getName(), set);
    }

    @Override
    public void removeLoginToken(String s) {
        tokenRepo.removeToken(s);
    }
}
