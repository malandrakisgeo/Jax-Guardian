import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Vetoed;
import javax.inject.Inject;
import javax.security.enterprise.AuthenticationException;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.authentication.mechanism.http.*;
import javax.security.enterprise.credential.RememberMeCredential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.IdentityStore;
import javax.security.enterprise.identitystore.IdentityStoreHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.security.enterprise.credential.Credential;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.SecurityContext;

import IdentityStore.FirstImplementation;

import java.util.Base64;

@ApplicationScoped
@AutoApplySession // For "Is user already logged-in?"
//@BasicAuthenticationMechanismDefinition
@Alternative //Avoiding org.jboss.weld.exceptions.AmbiguousResolutionException. See manuals
@Priority(100) //Ta @Alternative einai disabled apo mona tous. Prepei na dothei priority
//@Vetoed //Dinei to panw xeri sto default HttpAuthenticationMechanism tou org.glassfish.soteria, se wildfly 25
public class GMalAuthenticationMechanism implements HttpAuthenticationMechanism {

    @FirstImplementation
    @Inject
    private IdentityStore identityStore;

    @Override
    public AuthenticationStatus validateRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, HttpMessageContext httpMessageContext) throws AuthenticationException {
        Credential credential = httpMessageContext.getAuthParameters().getCredential();
        if(credential == null){
            credential = this.getCredentialsForBasicAuth(httpServletRequest);
        }

        httpServletResponse.setHeader(HttpHeaders.AUTHORIZATION,"value1"); //Ett sätt att sätta en token
        //httpServletResponse.addCookie();
        return httpMessageContext.notifyContainerAboutLogin(this.identityStore.validate(credential));
    }

   /* @Override
    public AuthenticationStatus secureResponse(HttpServletRequest request, HttpServletResponse response, HttpMessageContext httpMessageContext) throws AuthenticationException {
        return HttpAuthenticationMechanism.super.secureResponse(request, response, httpMessageContext);
    }

    @Override
    public void cleanSubject(HttpServletRequest request, HttpServletResponse response, HttpMessageContext httpMessageContext) {
        HttpAuthenticationMechanism.super.cleanSubject(request, response, httpMessageContext);
    }*/

    private Credential getCredentialsForBasicAuth(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String[] creds = !authorizationHeader.isEmpty() && authorizationHeader.startsWith("Basic ") ? (new String(Base64.getDecoder().decode(authorizationHeader.substring(6)))).split(":") : null;
        return (creds!=null && creds.length!=0) ? new UsernamePasswordCredential(creds[0], creds[1]) : null;
    }
}
