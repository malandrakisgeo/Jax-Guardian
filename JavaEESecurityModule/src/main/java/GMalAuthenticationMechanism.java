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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.SecurityContext;

import IdentityStore.FirstImplementation;
import login.GMalSessionListener;
import login.Login;
import login.LoginManager;
import tokens.Token;

import java.util.Base64;
import java.util.Collections;

@ApplicationScoped
@AutoApplySession // For "Is user already logged-in?"
@Alternative //Avoiding org.jboss.weld.exceptions.AmbiguousResolutionException. See manuals
@Priority(100) //Ta @Alternative einai disabled apo mona tous. Prepei na dothei priority
//@Vetoed //Dinei to panw xeri sto default HttpAuthenticationMechanism tou org.glassfish.soteria, se wildfly 25
public class GMalAuthenticationMechanism implements HttpAuthenticationMechanism {

    @Context
    SecurityContext securityContext;

    @FirstImplementation
    @Inject
    private IdentityStore identityStore;

    @FirstImplementation
    @Inject
    private RememberMeIdentityStore rememberMeIdentityStore;

    @Inject
    private LoginManager loginManager;


    @Override
    public AuthenticationStatus validateRequest(HttpServletRequest httpServletRequest,
                                                HttpServletResponse httpServletResponse,
                                                HttpMessageContext httpMessageContext) throws AuthenticationException {


        Credential credential = httpMessageContext.getAuthParameters().getCredential();

        if (credential == null) {
            credential = this.cookieForRemembermeCred(httpServletRequest);
        }
        if (credential == null) {
            credential = this.getCredentialsForBasicAuth(httpServletRequest);
        }


        if (credential instanceof UsernamePasswordCredential) {
            httpMessageContext.getRequest().getSession().setMaxInactiveInterval(20 * 60); //The session expires after 20minutes of inactivity.
            CredentialValidationResult credentialValidationResult = this.identityStore.validate(credential);

            if (credentialValidationResult.getStatus().equals(CredentialValidationResult.Status.VALID)) {
                httpServletRequest.getSession().setAttribute("login", new Login(this.loginManager)); //BINDING LOGIN TO SESSION!

                boolean login = this.manageLogin(httpMessageContext.getRequest().getSession(),
                        credentialValidationResult.getCallerPrincipal().getName(),
                        httpServletRequest.getHeader("USER-AGENT"),
                        httpServletRequest.getRemoteAddr(),
                        null);

                if (!login) {
                    credentialValidationResult = new CredentialValidationResult(String.valueOf(CredentialValidationResult.Status.INVALID));

                }

                if (login && httpServletRequest.getHeader("rememberMe") != null) {
                    String loginToken = this.rememberMeIdentityStore.generateLoginToken(credentialValidationResult.getCallerPrincipal(), credentialValidationResult.getCallerGroups());
                    //httpServletResponse.setHeader(HttpHeaders.AUTHORIZATION, loginToken); //Ett sätt att sätta en token
                    httpServletResponse.addCookie(new Cookie("GMAL_TOKEN", loginToken)); //Enallaktika
                }

            }

            return httpMessageContext.notifyContainerAboutLogin(credentialValidationResult);
        } else if (credential instanceof RememberMeCredential) {
            httpMessageContext.getRequest().getSession().setMaxInactiveInterval(60); //The session expires after 1 minute of inactivity.


            CredentialValidationResult credentialValidationResult = rememberMeIdentityStore.validate((RememberMeCredential) credential);

            if (credentialValidationResult.getStatus().equals(CredentialValidationResult.Status.VALID)) {
                httpServletRequest.getSession().setAttribute("login", new Login(this.loginManager)); //BINDING LOGIN TO SESSION!


                boolean login = this.manageLogin(httpMessageContext.getRequest().getSession(),
                        credentialValidationResult.getCallerPrincipal().getName(),
                        httpServletRequest.getHeader("USER-AGENT"),
                        httpServletRequest.getRemoteAddr(),
                        ((RememberMeCredential) credential).getToken());

                if (!login) {
                    credentialValidationResult = new CredentialValidationResult(String.valueOf(CredentialValidationResult.Status.INVALID));

                }
            }
            return httpMessageContext.notifyContainerAboutLogin(credentialValidationResult);
        }

        return httpMessageContext.doNothing(); //An dialekseis na epistrefei invalid, tote den tha mporeis oute na episkeftheis oute tis @PermitAll
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
        Credential credtoBeReturned;

        String username = null;
        String password = null;

        String authorizationHeader = request.getHeader("Authorization");
        String[] creds = !(authorizationHeader == null || authorizationHeader.isEmpty()) && authorizationHeader.startsWith("Basic ") ? (new String(Base64.getDecoder().decode(authorizationHeader.substring(6)))).split(":") : null;
        credtoBeReturned = (creds != null && creds.length != 0) ? new UsernamePasswordCredential(creds[0], creds[1]) : null;

        if (credtoBeReturned==null) {
            username = request.getHeader("Username");
            password = request.getHeader("Password");
            credtoBeReturned = (username!=null && password!=null) ? new UsernamePasswordCredential(username,password) : null;
        }

        return credtoBeReturned;

    }

    private Credential cookieForRemembermeCred(HttpServletRequest request) {
        Cookie[] cookielist = request.getCookies();
        String token = null;

        if (cookielist != null && cookielist.length > 0) {
            for (Cookie cookie : cookielist) {
                if (cookie.getName().equalsIgnoreCase("GMAL_TOKEN")) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        return token == null ? null : new RememberMeCredential(token);
    }

    private Credential takeCareOfTheJRemembermeId(HttpServletRequest request, HttpServletResponse response, String token) {
        Cookie[] arr = request.getCookies();
        for (Cookie cookie : arr) {
            if (cookie.getName().equalsIgnoreCase("jremembermeid")) {
            }
        }
        return null;
    }

    /*
        The code below works, because as soon as a session is invalidated, the user is regarded as inactive by the securitymodule
        even if the application itself will treat him as logged in.
     */
    private boolean manageLogin(HttpSession httpSession, String username, String useragent, String ipAddress, String tokenId) {
        Login login = this.loginManager.getloginfromsession(httpSession.getId()); //Each and every HttpSession has a corresponding "Login" object
        login.setAssociatedUsername(username);
        login.setIpAddress(ipAddress);
        login.setUserAgent(useragent);

        Login possibleOtherLogin = this.loginManager.getLoginForUser(username);

        if (possibleOtherLogin == null) { //If the user is not currently active from another session
            this.loginManager.addLogin(username, login); //the user logs in successfully
            return true;
        } else if (possibleOtherLogin.getAssociatedSession() == null) { //or perhaps if the user has a login with an expired session
            this.loginManager.removeLogin(username); //remove the loginobj with the expired session
            this.loginManager.addLogin(username, login);  //and replace it with this
            //TODO: Choose between this else if, and the if in the valueUnbound funtion of the Login object
        } else { //if the user is logged in from some other active session
            httpSession.invalidate(); //Invalidate both sessions
            possibleOtherLogin.invalidateSession();
            this.loginManager.removeLogin(username);

            if (tokenId != null) {
                this.rememberMeIdentityStore.removeLoginToken(tokenId); //along with the token, which can be regarded as compromised
            }
        }

        return false;
    }

    private void logoutManager(String username, String token){
        Login login = this.loginManager.getLoginForUser(username);
        if(login!=null){
            login.invalidateSession();
            this.loginManager.removeLogin(username);
        }

        if(token!=null){
            this.rememberMeIdentityStore.removeLoginToken(token);
        }
    }
}

