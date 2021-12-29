package users;

import java.util.Set;

public interface User {

    String getUsername();
    Set<String> getUserRoles();
    void setUsername(String username);
    void setUserRoles(Set<String>  roles);

}
