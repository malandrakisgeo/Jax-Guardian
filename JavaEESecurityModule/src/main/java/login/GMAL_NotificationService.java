package login;

public interface GMAL_NotificationService {
    void notifyUserForLogin(GMAL_LoginObject initialLogin, GMAL_LoginObject attemptedLogin);
}
