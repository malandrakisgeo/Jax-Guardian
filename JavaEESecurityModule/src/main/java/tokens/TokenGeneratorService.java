package tokens;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import users.User;

import javax.annotation.ManagedBean;
import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.Produces;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Set;
import java.util.UUID;

@ManagedBean //CDI injection
public class TokenGeneratorService{

    public String token(User user){
        /*
            TODO: Implement a token-generation based on encrypting the username, the roles, and a random value.
         */

        return null;
    }


    public String token(String username, Set<String> roles){
        String key = "SECRET_KEY";
        byte[] apiKeySecretBytes = Base64.getEncoder().encode(key.getBytes(StandardCharsets.UTF_8));
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());

        /* TODO: Fix this when you are not bored.
        JwtBuilder jwtBuilder = Jwts.builder()

                .claim("username", username)
                .claim("roles", roles)
                .signWith(SignatureAlgorithm.ES256, signingKey);
        return jwtBuilder.compact();*/

        return UUID.randomUUID().toString();
    }
}
