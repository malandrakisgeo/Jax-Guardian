package common;

import javax.annotation.security.DeclareRoles;
import javax.enterprise.context.ApplicationScoped;
import javax.security.enterprise.authentication.mechanism.http.AutoApplySession;
import javax.security.enterprise.authentication.mechanism.http.BasicAuthenticationMechanismDefinition;
import javax.security.enterprise.authentication.mechanism.http.CustomFormAuthenticationMechanismDefinition;
import javax.security.enterprise.identitystore.DatabaseIdentityStoreDefinition;
import javax.ws.rs.ApplicationPath;  //Wildfly will not initialize it if it is jakarta instead of javax!
import javax.ws.rs.core.Application;

@ApplicationPath("/rest")
@BasicAuthenticationMechanismDefinition
@ApplicationScoped
@DeclareRoles({"principal"})
public class JaxRsApplication extends Application {
    //The WildFly server initializes the application.
}
