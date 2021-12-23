import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import javax.security.enterprise.AuthenticationException;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.authentication.mechanism.http.*;
import javax.security.enterprise.credential.RememberMeCredential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;
import javax.security.enterprise.identitystore.RememberMeIdentityStore;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.security.enterprise.credential.Credential;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.HttpHeaders;

import IdentityStore.FirstImplementation;
import login.GMalSessionListener;
import login.Login;
import login.LoginManager;
import tokens.Token;

import java.util.Base64;

@ApplicationScoped
@AutoApplySession // For "Is user already logged-in?"
@RememberMe
@Alternative //Avoiding org.jboss.weld.exceptions.AmbiguousResolutionException. See manuals
@Priority(100) //Ta @Alternative einai disabled apo mona tous. Prepei na dothei priority
//@Vetoed //Dinei to panw xeri sto default HttpAuthenticationMechanism tou org.glassfish.soteria, se wildfly 25
public class GMalAuthenticationMechanism implements HttpAuthenticationMechanism {

    @FirstImplementation
    @Inject
    private IdentityStore identityStore;

    @FirstImplementation
    @Inject
    private RememberMeIdentityStore rememberMeIdentityStore;

    @Inject
    private LoginManager loginManager;


    @Override
    public AuthenticationStatus validateRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, HttpMessageContext httpMessageContext) throws AuthenticationException {

        Credential credential = httpMessageContext.getAuthParameters().getCredential();

        if (credential == null) {
            credential = this.getCredentialsForBasicAuth(httpServletRequest);
        }
        httpMessageContext.getRequest().getSession().setMaxInactiveInterval(15); //TEST!!!

        String str1 = httpServletRequest.getRequestedSessionId();
        String str2 = httpServletRequest.getSession().getId();
        httpServletRequest.getSession().getMaxInactiveInterval();
        String userAgent = httpServletRequest.getHeader("USER-AGENT");
        httpServletRequest.getRemoteAddr();


        if (credential instanceof UsernamePasswordCredential) {
            //httpMessageContext.getRequest().getSession().setMaxInactiveInterval(20 * 60); //The session expires after 20minutes of inactivity.
            CredentialValidationResult credentialValidationResult = this.identityStore.validate(credential);

            if (credentialValidationResult.getStatus().equals(CredentialValidationResult.Status.VALID)) {
                httpServletRequest.getSession().setAttribute("login", new Login()); //BINDING LOGIN TO SESSION!

                /*
                    TODO: Moiazei lathos na kaleis ena IdentityStore gia auth th douleia.
                    Alla den tha htan pio lathos an eprepe na ginei injection tou TokenGeneratorService se duo shmeia?
                 */
                String loginToken = this.rememberMeIdentityStore.generateLoginToken(credentialValidationResult.getCallerPrincipal(), credentialValidationResult.getCallerGroups());
                httpServletResponse.setHeader(HttpHeaders.AUTHORIZATION, loginToken); //Ett sätt att sätta en token
                httpServletResponse.addCookie(new Cookie("GMAL_TOKEN", loginToken)); //Enallaktika
            }

            return httpMessageContext.notifyContainerAboutLogin(credentialValidationResult);
        } else if (credential instanceof RememberMeCredential) {
            httpMessageContext.getRequest().getSession().setMaxInactiveInterval(60); //The session expires after 1 minute of inactivity.

            ((RememberMeCredential) credential).getToken();
            return httpMessageContext.notifyContainerAboutLogin(rememberMeIdentityStore.validate((RememberMeCredential) credential));
        }

        return httpMessageContext.notifyContainerAboutLogin(CredentialValidationResult.INVALID_RESULT);
    }

    @Override
    public AuthenticationStatus secureResponse(HttpServletRequest request, HttpServletResponse response, HttpMessageContext httpMessageContext) throws AuthenticationException {
        return HttpAuthenticationMechanism.super.secureResponse(request, response, httpMessageContext);
    }

    @Override
    public void cleanSubject(HttpServletRequest request, HttpServletResponse response, HttpMessageContext httpMessageContext) {
        HttpAuthenticationMechanism.super.cleanSubject(request, response, httpMessageContext);
    }

    private Credential getCredentialsForBasicAuth(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String[] creds = !authorizationHeader.isEmpty() && authorizationHeader.startsWith("Basic ") ? (new String(Base64.getDecoder().decode(authorizationHeader.substring(6)))).split(":") : null;
        return (creds != null && creds.length != 0) ? new UsernamePasswordCredential(creds[0], creds[1]) : null;
    }

    /*
        The code below works, because as soon as a session is invalidated, the user is regarded as inactive by the securitymodule
        even if the application itself will treat him as logged in.
     */
    private boolean manageLogin(HttpSession httpSession, String username, String useragent, String ipAddress, String tokenId) {
        Login login = this.loginManager.getloginfromsession(httpSession.getId());
        login.setAssociatedUsername(username);

        if (this.loginManager.getLoginForUser(username) == null) { //If the user is not currently active from another session
            this.loginManager.addLogin(username, login);  //then ok
            return true;
        }else{ //if the user is active from some other session
            this.loginManager.removeLogin(username); //invalidate it
            httpSession.invalidate(); //and invalidate this one too
            if(tokenId!=null){
                this.rememberMeIdentityStore.removeLoginToken(tokenId); //along with the token, which can be regarded as compromised
            }
        }

        return false;
    }
}

