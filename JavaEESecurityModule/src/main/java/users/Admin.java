package users;

import javax.enterprise.inject.Vetoed;
import java.util.Set;

@Vetoed //Avoid  WELD-001409: Ambiguous dependencies for type User with qualifiers @Default
public class Admin implements User{
    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public Set<String> getUserRoles() {
        return null;
    }
}
