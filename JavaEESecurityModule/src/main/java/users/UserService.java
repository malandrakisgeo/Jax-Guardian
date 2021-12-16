package users;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
import java.security.Principal;

import static java.util.Objects.nonNull;

@ApplicationScoped
public class UserService {
    @Inject
    SecurityContext context;

    @Produces
    @SessionScoped
    public User getUser() {
        final Principal principal = context.getCallerPrincipal();
        User result = null;

        /*if (nonNull(principal)) {

        }*/

        result = new UserMain();

        return result;
    }

}
