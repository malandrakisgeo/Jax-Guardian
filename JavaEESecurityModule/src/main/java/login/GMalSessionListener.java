package login;

import tokens.Token;

import javax.inject.Inject;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener
public class GMalSessionListener implements HttpSessionListener {
    /*
        Einai WebListener gia HttpSession. Dld, tha trexei *gia kathe* session aneksarthta apo to an telika ginei login h oxi.
     */

    @Override
    public void sessionCreated(HttpSessionEvent se) {
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {

    }


}
