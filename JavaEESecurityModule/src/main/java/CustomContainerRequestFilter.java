import javax.annotation.Priority;
import javax.inject.Inject;
import javax.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.security.Principal;


@Provider
@Priority(Priorities.AUTHORIZATION)
public class CustomContainerRequestFilter implements ContainerRequestFilter {


    @Context
    private ResourceInfo resourceInfo;

    @Context
    private SecurityContext securityContext;

    @Context
    private HttpMessageContext httpMessageContext;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) {
        final Principal principal = containerRequestContext.getSecurityContext().getUserPrincipal();

        containerRequestContext.getRequest();
        //session.invalidate();

    }
}