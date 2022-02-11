package login;

public interface JaxG_NotificationService {
    void notifyUserForLogin(JaxG_LoginObject initialLogin, JaxG_LoginObject attemptedLogin);
}
