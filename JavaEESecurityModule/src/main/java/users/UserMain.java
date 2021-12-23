package users;

import javax.enterprise.inject.Vetoed;
import java.util.Set;

@Vetoed //Avoid  WELD-001409: Ambiguous dependencies for type User with qualifiers @Default
public class UserMain implements User{
    @Override
    public String getUsername() {
        return "GEORGE";
    }

    @Override
    public Set<String> getUserRoles() {
        return null;
    }
}
