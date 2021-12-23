package tokens;

import users.User;

import javax.annotation.ManagedBean;
import javax.ws.rs.Produces;
import java.util.Set;

@ManagedBean //CDI injection
public class TokenGeneratorService{

    public String token(User user){
        /*
            TODO: Implement a token-generation based on encrypting the username, the roles, and a random value.
         */

        return null;
    }


    public String token(String username, Set<String> roles){
        /*
            TODO: Implement a token-generation based on encrypting the username, the roles, and a random value.
         */

        return null;
    }
}
