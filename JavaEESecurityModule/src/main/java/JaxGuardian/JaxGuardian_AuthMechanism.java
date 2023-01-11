package JaxGuardian;

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

import IdentityStore.FirstImplementation;
import login.JaxG_NotificationService;
import login.JaxG_LoginObject;
import login.JaxG_LoginManager;

import java.io.IOException;
import java.util.Base64;

@ApplicationScoped
@AutoApplySession // For "Is user already logged-in?"
@Alternative //Avoiding org.jboss.weld.exceptions.AmbiguousResolutionException. See manuals
@Priority(100) //Ta @Alternative einai disabled apo mona tous. Prepei na dothei priority
//@Vetoed //Dinei to panw xeri sto default HttpAuthenticationMechanism tou org.glassfish.soteria, se wildfly 2
public class JaxGuardian_AuthMechanism implements HttpAuthenticationMechanism {

    @FirstImplementation
    @Inject
    private IdentityStore identityStore;

    @FirstImplementation
    @Inject
    private RememberMeIdentityStore rememberMeIdentityStore;

    @Inject
    private JaxG_LoginManager jaxGLoginManager;

    @Inject
    private JaxG_NotificationService jaxG_notificationService;

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
            CredentialValidationResult credentialValidationResult = this.identityStore.validate(credential);

            if (credentialValidationResult.getStatus().equals(CredentialValidationResult.Status.VALID)) {
                if(httpServletRequest.getHeader("rememberMe") == null){
                    httpMessageContext.getRequest().getSession().setMaxInactiveInterval(20 * 60); //The session expires after 20minutes of inactivity.
                }
                httpServletRequest.getSession().setAttribute("login", new JaxG_LoginObject(this.jaxGLoginManager)); //BINDING LOGIN TO SESSION!

                JaxG_LoginObject loginObject = this.manageLogin(httpMessageContext,
                        credentialValidationResult.getCallerPrincipal().getName(),
                        httpServletRequest.getHeader("USER-AGENT"),
                        httpServletRequest.getRemoteAddr(),
                        null);


                if (loginObject == null) {
                //TODO: sth
                   // credentialValidationResult = new CredentialValidationResult(String.valueOf(CredentialValidationResult.Status.INVALID));
                    //httpMessageContext.getRequest().getSession().invalidate();
                }

                if (loginObject!=null && httpServletRequest.getHeader("rememberMe") != null) {
                    httpMessageContext.getRequest().getSession().setMaxInactiveInterval(20); //The session expires after 20seconds of inactivity, if the user has chosen rememberMe.
                    String loginToken = this.rememberMeIdentityStore.generateLoginToken(credentialValidationResult.getCallerPrincipal(), credentialValidationResult.getCallerGroups());
                    loginObject.setToken(loginToken);
                    httpServletResponse.addCookie(new Cookie("GMAL_TOKEN", loginToken)); //Enallaktika
                    return httpMessageContext.notifyContainerAboutLogin(credentialValidationResult);
                }
                
            }


        } else if (credential instanceof RememberMeCredential) {
            httpMessageContext.getRequest().getSession().setMaxInactiveInterval(20); //The session expires after 20seconds of inactivity.


            CredentialValidationResult credentialValidationResult = rememberMeIdentityStore.validate((RememberMeCredential) credential);

            if (credentialValidationResult.getStatus().equals(CredentialValidationResult.Status.VALID)) {
                httpServletRequest.getSession().setAttribute("login", new JaxG_LoginObject(this.jaxGLoginManager)); //BINDING LOGIN TO SESSION!

                JaxG_LoginObject loginObject = this.manageLogin(httpMessageContext,
                        credentialValidationResult.getCallerPrincipal().getName(),
                        httpServletRequest.getHeader("USER-AGENT"),
                        httpServletRequest.getRemoteAddr(),
                        ((RememberMeCredential) credential).getToken());


                if (loginObject==null) {
                    credentialValidationResult = new CredentialValidationResult(String.valueOf(CredentialValidationResult.Status.INVALID));
                    httpMessageContext.getRequest().getSession().invalidate();
                }else{
                    return httpMessageContext.notifyContainerAboutLogin(credentialValidationResult);
                }
            }
        }


        httpMessageContext.getRequest().getSession().invalidate();
        return httpMessageContext.responseUnauthorized(); //An dialekseis na epistrefei invalid, tote den tha mporeis oute na episkeftheis
        // oute tis @PermitAll
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

        if (credtoBeReturned == null) {
            username = request.getHeader("Username");
            password = request.getHeader("Password");
            credtoBeReturned = (username != null && password != null) ? new UsernamePasswordCredential(username, password) : null;
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


    /*
        The code below works, because as soon as a session is invalidated, the user is regarded as inactive by the securitymodule
        even if the application itself will treat him as logged in.

        A LoginObject will be returned if the user is not already active from another session.
     */
    private JaxG_LoginObject manageLogin(HttpMessageContext httpMessageContext, String username, String useragent, String ipAddress, String tokenId) {
        HttpSession httpSession = httpMessageContext.getRequest().getSession();

        JaxG_LoginObject jaxGLoginObject = this.jaxGLoginManager.getloginfromsession(httpSession.getId()); //Each and every HttpSession has a corresponding "Login" object
        jaxGLoginObject.setAssociatedUsername(username);
        jaxGLoginObject.setIpAddress(ipAddress);
        jaxGLoginObject.setUserAgent(useragent);
        jaxGLoginObject.setToken(tokenId); //null if the user logged in with credentials instead of token

        JaxG_LoginObject possibleOtherLogin = this.jaxGLoginManager.getLoginForUser(username);

        if (possibleOtherLogin == null) { //If the user is not currently active from another session

            this.jaxGLoginManager.addLogin(username, jaxGLoginObject); //the user logs in successfully
            return jaxGLoginObject;
        } else { //if the user is logged in from some other active session, invalidate both logins.

            /* NOTE:  The initial version included these lines:
            httpSession.invalidate();
            possibleOtherLogin.invalidateSession();

            Both would invoke the valueUnbound() function of their related LoginObject, thereby removing
            their IDs from the GMALLoginManager.
            But that resulted to a bug, because if the current session is invalidated before all related functions in the
            AuthMechanism complete, a new one  would be created automatically for the rest of the procedure! It will
            include the same credentials, and create a JaxG_LoginObject too, hence making the user appear as logged in
            despite the removal of his login-object &  related sessions.

            So instead of manually invalidating the sessions, we will directly remove their IDs from the LoginManager
            here.

            */


            this.jaxGLoginManager.removeSession(httpSession.getId());
            this.jaxGLoginManager.removeSession(possibleOtherLogin.getAssociatedSession().getId());
            this.jaxGLoginManager.removeLogin(username);

            if (tokenId != null) {
                this.rememberMeIdentityStore.removeLoginToken(tokenId); //Invalidate the token too, which can be regarded
                // as compromised
            }

            try {
                this.jaxG_notificationService.notifyUserForLogin(jaxGLoginObject, possibleOtherLogin); //Notify the user via email
                httpMessageContext.getResponse().sendRedirect(httpMessageContext.getRequest().getContextPath() + httpMessageContext.getRequest().getServletPath()+ "/authError/ERROR/" + username); //and return a proper response
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;

    }

}

