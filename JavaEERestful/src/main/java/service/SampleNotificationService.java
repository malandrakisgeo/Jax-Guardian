package service;

import login.JaxG_LoginObject;
import login.JaxG_NotificationService;

import javax.annotation.ManagedBean;

@ManagedBean
public class SampleNotificationService implements JaxG_NotificationService {

    @Override
    public void notifyUserForLogin(JaxG_LoginObject initialLogin, JaxG_LoginObject attemptedLogin) {
        /*
            Sends an email with details of the initial and the attempted login (IP/location, time, etc).
            Adjust according to your needs.
         */
    }
}
