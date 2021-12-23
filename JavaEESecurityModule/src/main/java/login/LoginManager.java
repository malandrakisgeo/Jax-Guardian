package login;

import javax.enterprise.context.ApplicationScoped;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

@ApplicationScoped
public class LoginManager {

    private HashMap<String, Login> userLogins = new HashMap<>();
    private HashMap<String, Login> activeSessions = new HashMap<>();

    /*
        1o senario: login me kwdiko kai username:
            1. Elegxetai an einai swsta
            2. Ginetai elegxos an o xrhsths einai hdh logged in apo allou.
            3. An ola einai ok kai o xrhsths exei epileksei rememberMe, dhmiourgeitai token pou epistrefetai ston xrhsth. Eidallws arkoumaste sto jsessionid

        2o senario: login me token
            1. Elegxetai an einai uparkto kai egkuro kai fetcharontai ta stoixeia tou xrhsth.
            2. Ginetai elegxos an o xrhsths einai hdh logged in apo allou.
            3. An ola einai ok, o xrhsths sunexizei.
            (4. Na allazei to token periodika isws?)

        Problem: Pws elegxoume an o xrhsths einai hdh logged in apo allou?
        Exoume username, token, session, .getRemoteAddr(), kai user-agent.

        Pithanh lush 1: Ena hashmap User/Login, opou login mia klash me ta alla stoixeia kai mia boolean metavlhth isActiveNow.
        An enas xrhsths kanei login kai uparxei hdh sth lista me isActiveNow==true, kai ta duo sessions ginontai invalidated.
        To isActiveNow kathorizetai apo to an uparxei egkuro session me allh dieuthunsh h user-agent.

        Provlhma sth lush 1: Mia kataxwrhsh sto hashmap den ginetai na meinei ekei esaei.
        Prepei na diagrafontai taktika oi kataxwrhseis, sugkekrimena opote to session lhgei.
        An kserame me sigouria oti tha htan efarmogh gia 5 malakes tha ginotan eukola me mia forEach pou trexei kathe p.x. 5 lepta,
        alla pera apo to oti kai tote tha htan paraplhgikos tropos, den kseroume kan poses kataxwrhseis tha exoume. An einai 100.000?
        Mallon h lush tha epelthei apo Events.

     */

    public void addLogin(String username, Login login){
        this.userLogins.put(username, login);
    }
    public void removeLogin(String username){
        this.userLogins.remove(username);
    }

    public Login getLoginForUser(String username){
        return this.userLogins.get(username);
    }

    public void addSession(String sessionId, Login loginEntity){
        this.activeSessions.put(sessionId, loginEntity);
    }
    public void removeSession(String sessionId){
        this.activeSessions.remove(sessionId);
    }

    public Login getloginfromsession(String sessionId){
        return this.activeSessions.get(sessionId);
    }



}
