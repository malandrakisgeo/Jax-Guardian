import javax.annotation.Priority;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.security.Principal;


@PreMatching
public class CustomContainerRequestFilter implements ContainerRequestFilter {


    @Inject
    HttpSession session;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) {
        final Principal principal = containerRequestContext.getSecurityContext().getUserPrincipal();


        final Response response =
                Response.status(Response.Status.FORBIDDEN)
                        .type(MediaType.APPLICATION_JSON_TYPE)
                        .build();
        containerRequestContext.abortWith(response);
        session.invalidate();

    }
}