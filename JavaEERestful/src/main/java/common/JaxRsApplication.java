package common;

import javax.annotation.security.DeclareRoles;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.ApplicationPath;  //Wildfly will not initialize it if it is jakarta instead of javax!
import javax.ws.rs.core.Application;

@ApplicationPath("/rest")
@ApplicationScoped
@DeclareRoles({"principal"})
public class JaxRsApplication extends Application {
    //The WildFly server initializes the application.
}
