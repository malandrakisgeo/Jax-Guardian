package IdentityStore;

import credentials.TokenCredential;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.security.enterprise.SecurityContext;
import javax.security.enterprise.authentication.mechanism.http.AutoApplySession;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.BasicAuthenticationCredential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;
import javax.ws.rs.ext.Provider;
import users.Token;
import users.TokenRepo;
import users.User;
import users.UserRepo;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
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
        //userRepo.getUser("a", "b");
        CredentialValidationResult credentialValidationResult;
        if(credential instanceof TokenCredential){
            Token token = this.tokenRepo.getToken(credential.toString());
            if(token==null){
                return CredentialValidationResult.INVALID_RESULT;
            }
            if(token.isActive()){
                //Invalidate token and remove it from the db.
                credentialValidationResult = new CredentialValidationResult(token.getRelatedUser());
                this.tokenRepo.removeToken(token);
                return CredentialValidationResult.INVALID_RESULT;
                //TODO: Consider taking other measures, such as informing the user that someone might have compromised their account,
            }

            //Decode token and fetch roles of user.
        }

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
            //Fetch roles of the user and generate a token
            //Save the token in the database
        }

        return CredentialValidationResult.INVALID_RESULT;
    }

    @Override
    public Set<String> getCallerGroups(CredentialValidationResult validationResult) {
        return validationResult.getCallerGroups();
    }
}
