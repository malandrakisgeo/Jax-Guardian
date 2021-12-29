package IdentityStore;

import tokens.Token;
import tokens.TokenGeneratorService;
import tokens.TokenRepo;
import tokens.TokenSampleImpl;
import users.User;
import users.UserSampleImpl;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.security.enterprise.CallerPrincipal;
import javax.security.enterprise.authentication.mechanism.http.RememberMe;
import javax.security.enterprise.credential.RememberMeCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.RememberMeIdentityStore;
import java.util.Calendar;
import java.util.Collections;
import java.util.Set;

@ApplicationScoped
@FirstImplementation
@RememberMe(cookieName = "GMAL_TOKEN")
public class RememberMeIdentityStoryImpl implements RememberMeIdentityStore {

    @Inject
    TokenGeneratorService tokenGen;

    @Inject
    @Singleton
    TokenRepo tokenRepo;

    @Override
    public CredentialValidationResult validate(RememberMeCredential credential) {
        CredentialValidationResult credentialValidationResult1 = CredentialValidationResult.INVALID_RESULT;
        Token token = this.getTokenFromId(credential.getToken());

        if (token != null && token.isActive()) {
            credentialValidationResult1 = new CredentialValidationResult(credential.getToken(), token.getRelatedUser().getUsername(), null, credential.getToken(), Collections.singleton("principal"));
        }

        return credentialValidationResult1;
    }

    //Generates a JREMEMBERMEID, i.e. a cookie with a particular value.
    /*Trexei panta meta to httpMessageContext.notifyContainerAboutLogin(credentialValidationResult) an uparxei @RememberMe kai sto authmechanism*/
    @Override
    public String generateLoginToken(CallerPrincipal callerPrincipal, Set<String> roles) {
        Token tokk = this.tokenRepo.getTokenForUser(null);
        if (tokk != null && tokk.isActive()) {
            return tokk.getValue();
        }
        Calendar calendar = Calendar.getInstance(); // gets a calendar using the default time zone and locale.
        calendar.add(Calendar.SECOND, 86400);

        Token tok = new TokenSampleImpl(this.tokenGen.token(callerPrincipal.getName(), roles));
        User usr = new UserSampleImpl();
        usr.setUsername(callerPrincipal.getName());
        usr.setUserRoles(roles);
        tok.setRelatedUser(usr);
        tok.setActiveUntil(calendar.getTime());
        Token token = tokenRepo.createToken(tok);

        return token.getValue();
    }

    @Override
    public void removeLoginToken(String s) {
        tokenRepo.removeToken(s);
    }

    public Token getTokenFromId(String tokenId) {
        return tokenRepo.getToken(tokenId);
    }
}
