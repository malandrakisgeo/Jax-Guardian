package login;

import tokens.TokenRepo;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;

public class GMAL_LoginManager {

    @Inject
    @Singleton
    TokenRepo tokenRepo;

    private HashMap<String, GMAL_LoginObject> userLogins = new HashMap<>();
    private HashMap<String, GMAL_LoginObject> activeSessions = new HashMap<>();

    public void addLogin(String username, GMAL_LoginObject GMALLoginObject) {
        this.userLogins.put(username, GMALLoginObject);
    }

    public void removeLogin(String username) {
        this.userLogins.remove(username);
    }

    public GMAL_LoginObject getLoginForUser(String username) {
        return this.userLogins.get(username);
    }

    public void addSession(String sessionId, GMAL_LoginObject loginEntity) {
        this.activeSessions.put(sessionId, loginEntity);
    }

    public void removeSession(String sessionId) {
        this.activeSessions.remove(sessionId);
    }

    public GMAL_LoginObject getloginfromsession(String sessionId) {
        return this.activeSessions.get(sessionId);
    }


    public void logout(String username) {
        GMAL_LoginObject GMALLoginObject = this.getLoginForUser(username);

        if (GMALLoginObject != null) {
            GMALLoginObject.invalidateSession();
            this.removeLogin(username);
            if (GMALLoginObject.getToken() != null) {
                this.tokenRepo.removeToken(GMALLoginObject.getToken());
            }
        }

    }

}
