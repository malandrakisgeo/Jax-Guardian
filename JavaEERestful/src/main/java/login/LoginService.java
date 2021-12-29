package login;

import entity.TokenExample;
import repository.TokenRepository;
import repository.UserRepository;
import tokens.Token;
import users.User;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@ApplicationScoped
public class LoginService {


    @Inject
    @Singleton
    TokenRepository tokenRepository;

    @Inject
    UserRepository userRepository;

    //Just for some tests. NOT a real way of logging in. TODO: Vidareutveckla eller ta bort.
    public Response login(String username, String password, boolean rememberMe) {
        User user = userRepository.getUser(username, password);
        if (user != null) {
            if (rememberMe) {

                Token token = new TokenExample(UUID.randomUUID().toString());
                token.setRelatedUser(user);
                Calendar calendar = Calendar.getInstance(); // gets a calendar using the default time zone and locale.
                calendar.add(Calendar.SECOND, 600); //10 minutes

                token.setActiveUntil(calendar.getTime());

                 //tokenRepository.createToken(token); //Kati tetoio tha htan suntagh gia askoph dhmiourgia tokens sth vash, parakamptontas malista tis diadikasies tou loginManager

                return Response.accepted().header("Authorization", token.getValue()).build();
            }
        }

        return Response.noContent().build();


    }

}
