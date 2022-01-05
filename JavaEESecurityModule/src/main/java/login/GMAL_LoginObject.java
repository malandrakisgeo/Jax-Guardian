package login;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import java.io.Serializable;

//@ManagedBean
public class GMAL_LoginObject implements HttpSessionBindingListener, Serializable {

    /*
        Auth h klash einai HttpSessionBindingListener.
        Toutestin, tha trexei an kai efoson ginei to binding me kapoio session.

        Auto se antidiastolh me tis HttpSessionListener, pou trexoun gia kathe session aneksarthta apo to an ginei telika login h oxi.s
     */

    GMAL_LoginManager GMALLoginManager;

    private HttpSession associatedSession;
    private String ipAddress;
    private String userAgent;
    private String token;
    private String associatedUsername; //TODO: Fundera ytterligare om det verkligen behövs här.

    public GMAL_LoginObject(GMAL_LoginManager GMALLoginManager){
        this.GMALLoginManager = GMALLoginManager;
    }

    //Runs when the session is created.
    @Override
    public void valueBound(HttpSessionBindingEvent event) {
        this.associatedSession = event.getSession();
        this.GMALLoginManager.addSession(this.associatedSession.getId(), this);
    }


    //Runs before the session is destroyed.
    @Override
    public void valueUnbound(HttpSessionBindingEvent event) {
        this.GMALLoginManager.removeSession(this.associatedSession.getId());
        //TODO: Perhaps temporarily (e.g. for a month) save  details about the session or the login for security reasons in some db?
        if(this.GMALLoginManager.getLoginForUser(this.associatedUsername)!= null && this.GMALLoginManager.getLoginForUser(this.associatedUsername).hashCode() == this.hashCode()){
            this.GMALLoginManager.removeLogin(this.associatedUsername);
        }
        //this.associatedSession = null;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }

    public HttpSession getAssociatedSession() {
        return associatedSession;
    }


    public void invalidateSession(){
        try{
            this.associatedSession.invalidate();
        } catch(IllegalStateException e){ //Session already invalidated
            this.GMALLoginManager.removeSession(this.associatedSession.getId());
            /*TODO: There was a case when, for some strange reason, the session wasn't removed even
                though it was invalid and  valueUnbound ran (?). Check again if it actually was so and we really need this.
            */
        }
    }
}
