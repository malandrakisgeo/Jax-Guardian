package endpoints;

import login.JaxG_LoginManager;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("")
@PermitAll
public class LogoutEndpoint {
    @Inject
    JaxG_LoginManager loginManager;

    @Context
    SecurityContext securityContext;

    @GET
    @Path("/logout")
    public Response logout() {
        if(this.securityContext.getUserPrincipal() !=null){
            this.loginManager.logout(this.securityContext.getUserPrincipal().getName());
        };

        return Response.accepted().build();
    }

}
