package endpoints;

import javax.annotation.security.PermitAll;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/authError")
@PermitAll
public class SampleJaxRsEndpointForAuthenticationErrors {

    @GET
    @POST
    @PUT
    @Path("/ERROR/{username}")
    public Response endpointForGetRequests(@PathParam("username") String username){
        return this.userAlreadyAuthorized(username);
    }


    private Response userAlreadyAuthorized(String username){
       /*
            Insert own code for the email, the error message, osv
         */
        return Response.status(Response.Status.FORBIDDEN).entity("You seem to be already logged in from another session. Check your email for further details").build();

    }
}
