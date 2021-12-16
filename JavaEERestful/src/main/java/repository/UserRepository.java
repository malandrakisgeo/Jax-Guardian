package repository;


import users.User;
import users.UserMain;
import users.UserRepo;

public class UserRepository implements UserRepo {
    @Override
    public User getUser(String username, String password) {
        if(!username.equalsIgnoreCase("principal")){
            return null;
        }
        return new UserMain();
    }
}
