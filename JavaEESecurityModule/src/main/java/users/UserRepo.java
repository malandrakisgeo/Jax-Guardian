package users;


public interface UserRepo {

    public User getUser(String username, String password);
}
