package service;

import login.GMAL_NotificationService;
import login.GMAL_LoginObject;

import javax.annotation.ManagedBean;

@ManagedBean
public class SampleNotificationService implements GMAL_NotificationService {

    @Override
    public void notifyUserForLogin(GMAL_LoginObject initialLogin, GMAL_LoginObject attemptedLogin) {
        /*
            Sends an email with details of the initial and the attempted login (IP/location, time, etc).
            Adjust according to your needs.
         */
    }
}
