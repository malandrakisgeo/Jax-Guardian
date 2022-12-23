package endpoints;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
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

    @GET
    @Path("/getCreator")
    @RolesAllowed({"principal"})
    public Response getTeachers() {
       return Response.accepted().entity("George Malandrakis!").build();
    }


    @GET
    @Path("/getTeacherserror")
    public Response getTeachers232(){
        if(securityContext.isUserInRole("principal")){
            //return Response.accepted().entity(user.getUsername()).build();
        }

        return Response.noContent().status(Response.Status.UNAUTHORIZED).build();
    }
}

