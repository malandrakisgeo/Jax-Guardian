package common;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.ApplicationPath;  //Wildfly will not initialize it if it is jakarta instead of javax!
import javax.ws.rs.core.Application;


@ApplicationPath("/rest")
@ApplicationScoped
public class JaxRsApplication extends Application {
    //The WildFly server initializes the application.

    /*public Set<Class<?>> getClasses() {
        Set<Class<?>> s = new HashSet<Class<?>>();
        s.add(JaxRsEndpoints.class);
        return s;
    }*/
}
