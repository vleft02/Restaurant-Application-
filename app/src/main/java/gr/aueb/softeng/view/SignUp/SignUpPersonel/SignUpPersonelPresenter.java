package gr.aueb.softeng.view.SignUp.SignUpPersonel;

import java.util.HashMap;
import java.util.Map;

import gr.aueb.softeng.dao.ChefDAO;

import gr.aueb.softeng.dao.UserDAO;
import gr.aueb.softeng.domain.Chef;


public class SignUpPersonelPresenter {
    private ChefDAO chefDAO;
    private UserDAO userDAO;
    SignUpPersonelView view;
    public SignUpPersonelPresenter(UserDAO userDAO, ChefDAO chefDAO)
    {
        this.chefDAO = chefDAO;
        this.userDAO = userDAO;
    }
    public void setView(SignUpPersonelView v)
    {
        this.view = v;
    }

    public SignUpPersonelView getView() {
        return view;
    }

    public void onCreateChefAccount(){
        boolean isEmpty=false;
        HashMap<String,String> details = view.getChefDetails();

        for(Map.Entry<String, String> set: details.entrySet()){
            if(set.getValue().isEmpty() || set.getValue()==null){
                isEmpty=true;
                break;
            }
        }
        if(isEmpty){
            view.showErrorMessage("Σφάλμα!", "Συμπληρώστε όλα τα πεδία!.");
        }else if(details.get("username").length() < 4 || details.get("username").length() > 15) {
            view.showErrorMessage("Σφάλμα!", "Συμπληρώστε 4 έως 15 χαρακτήρες στο Username.");
        }else if(!details.get("email").contains("@") &&( !details.get("email").contains(".com") || !details.get("email").contains(".gr"))) {
            view.showErrorMessage("Σφάλμα!", "Συμπληρώστε σωστά το email.");
        }else if (details.get("telephone").length() != 10) {
            view.showErrorMessage("Σφάλμα!", "Συμπληρώστε έγκυρο τηλεφωνικό αριθμό.");
        }else if (details.get("iban").length() != 34) { // anti gia 5 na valw akrivws posa einai pragmatika
            view.showErrorMessage("Σφάλμα!", "Συμπληρώστε έγκυρο iban με 34 ψηφία");
        }else if (details.get("password").length() < 8) {
            view.showErrorMessage("Σφάλμα!", "Ο κωδικός θα πρέπει να αποτελείται απο 8 ψηφία και πάνω.");
        }else if (details.get("tin").length() != 9){
            view.showErrorMessage("Σφάλμα!", "Συμπληρώστε έγκυρο αφμ με 9 ψηφία.");
        }else if (userDAO.find(details.get("username"))!=null){ // there is already a user with the same username and password
            view.showErrorMessage("Σφάλμα!","Υπάρχει ήδη λογαριασμός με αυτο το username \n Συμπληρώστε νέα στοιχεία!" );
        }else{

            Chef chef= new Chef(details.get("username"),details.get("name"),details.get("surname"),details.get("telephone"),
                    details.get("email"),details.get("password"), chefDAO.nextId(),details.get("iban"),details.get("tin"));

            chefDAO.save(chef);
            userDAO.save(chef);

            view.showAccountCreatedMessage();
        }

    }
    public void onBack(){
        view.goBack();
    }
}
