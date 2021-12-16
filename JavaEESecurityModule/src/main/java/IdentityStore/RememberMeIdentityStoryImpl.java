package IdentityStore;

import credentials.TokenCredential;
import users.Token;
import users.TokenRepo;
import users.User;
import users.UserRepo;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.CallerPrincipal;
import javax.security.enterprise.credential.RememberMeCredential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.RememberMeIdentityStore;
import java.util.Collections;
import java.util.Set;

@ApplicationScoped
public class RememberMeIdentityStoryImpl implements RememberMeIdentityStore {

    @Inject
    UserRepo userRepo;

    @Inject
    TokenRepo tokenRepo;

    @Override
    public CredentialValidationResult validate(RememberMeCredential credential) {
        return null;
    }

    @Override
    public String generateLoginToken(CallerPrincipal callerPrincipal, Set<String> set) {
        return null;
    }

    @Override
    public void removeLoginToken(String s) {

    }
}
