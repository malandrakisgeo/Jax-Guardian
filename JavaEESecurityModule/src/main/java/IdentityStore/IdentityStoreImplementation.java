package IdentityStore;

import javax.enterprise.context.ApplicationScoped;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;

import tokens.TokenRepo;
import users.User;
import users.UserRepo;

import javax.inject.Inject;
import java.util.Collections;
import java.util.Set;

@FirstImplementation
@ApplicationScoped
public class IdentityStoreImplementation implements IdentityStore {

    @Inject
    UserRepo userRepo;

    @Inject
    TokenRepo tokenRepo;


    @Override
    public int priority() {
        return 10;
    }

    @Override
    public CredentialValidationResult validate(Credential credential){
        CredentialValidationResult credentialValidationResult;

        if(credential instanceof UsernamePasswordCredential){
            User user = this.userRepo.getUser(((UsernamePasswordCredential) credential).getCaller(), ((UsernamePasswordCredential) credential).getPasswordAsString());
            if(user==null){
                return CredentialValidationResult.INVALID_RESULT;
            }else{
                CredentialValidationResult credentialValidationResult1 = new CredentialValidationResult(((UsernamePasswordCredential) credential).getCaller(), Collections.singleton("principal") );
                credentialValidationResult1.getCallerGroups();
                CredentialValidationResult.Status stt =credentialValidationResult1.getStatus();
                return credentialValidationResult1;
            }
        }

        return CredentialValidationResult.INVALID_RESULT;
    }

    @Override
    public Set<String> getCallerGroups(CredentialValidationResult validationResult) {
        return validationResult.getCallerGroups();
    }
}
