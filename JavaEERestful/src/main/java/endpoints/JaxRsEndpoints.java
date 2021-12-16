package endpoints;

import users.User;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.security.enterprise.authentication.mechanism.http.AutoApplySession;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;

import javax.ws.rs.Path; //Wildfly will not initialize it if it is jakarta instead of javax!
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

//@RequestScoped
@Path("/endpoint")
public class JaxRsEndpoints {
/*
    Gia na doulepsei se wildfly prepei to INTEGRATED jaspi gia to security-domain na einai off, enw to Jaspi genikotera on:
    https://stackoverflow.com/questions/70225352/why-does-this-simple-jakarta-security-example-from-soteria-work-on-payara-but-no

    Auto vasika shmainei oti sto standalone-full prepei na uparxei auto:
            <application-security-domains>
                <application-security-domain name="other" security-domain="ApplicationDomain" enable-jacc="false" enable-jaspi="true" integrated-jaspi="false"/>
            </application-security-domains>
 */
    @Context
    SecurityContext securityContext;

    @Inject
    User user;

    @GET
    @Path("/getTeachers")
    @RolesAllowed({"principal"})
    public Response getTeachers(){
        return Response.accepted().entity("George Malandrakis!").build();
    }

    @GET
    @Path("/getTeachers2")
    public Response getTeachers2(){
        if(securityContext.isUserInRole("principal")){
            //return Response.accepted().entity(user.getUsername()).build();
        }

        return Response.noContent().status(Response.Status.UNAUTHORIZED).build();
    }

    @POST
    @PermitAll
    @Path("/login")
    public Response login(@HeaderParam("username") String username, @HeaderParam("password") String password ){
        /*
        * */

        return null;
    }

}

