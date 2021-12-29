package endpoints;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import login.LoginService;

@Path("/login")
@PermitAll
public class login {

    @Inject
    LoginService loginService;
    @GET
    @Path("/test")
    public Response test(
    ){
        return Response.noContent().build();
    }

    @GET
    @Path("")
    public Response login(@PathParam("username") String username,
                          @PathParam("password") String password,
                          @PathParam("rememberMe") String rememberMe

    ){
        return  loginService.login(username,password,Boolean.valueOf(rememberMe));
    }

    @POST
    @Path("")
    public Response loginpost(@HeaderParam("username") String username,
                          @HeaderParam("password") String password,
                          @HeaderParam("rememberMe") String rememberMe
    ){
        return  loginService.login(username,password,Boolean.valueOf(rememberMe));
    }
}
