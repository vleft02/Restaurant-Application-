package gr.aueb.softeng.view.SignUp.SignUpCustomer;

import java.util.HashMap;
import java.util.Map;

import gr.aueb.softeng.dao.CustomerDAO;
import gr.aueb.softeng.dao.UserDAO;
import gr.aueb.softeng.domain.Customer;

public class SignUpCustomerPresenter {
    private CustomerDAO customerDAO;
    private  UserDAO userDAO;

    public SignUpCustomerPresenter(UserDAO userDAO, CustomerDAO custDAO)
    {
        this.customerDAO = custDAO;
        this.userDAO=userDAO;
    }
    SignUpCustomerView view;
    public void setView(SignUpCustomerView v)
    {
        this.view = v;
    }

    public SignUpCustomerView getView() {
        return view;
    }

    public void onCreateAccount(){
        boolean isEmpty=false;
        HashMap<String,String> details = view.getDetails();

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
        }else if (details.get("telephone").length() != 10 ) {
            view.showErrorMessage("Σφάλμα!", "Συμπληρώστε έγκυρο τηλεφωνικό αριθμό.");
        }else if (details.get("cardNumber").length() != 16 || details.get("cardNumber").matches("\\d+")) { // anti gia 5 na valw akrivws posa einai pragmatika
            view.showErrorMessage("Σφάλμα!", "Συμπληρώστε έγκυρο αριθμό κάρτας με 16 ψηφία");
        }else if (details.get("password").length() < 8) {
            view.showErrorMessage("Σφάλμα!", "Ο κωδικός θα πρέπει να αποτελείται απο 8 ψηφία και πάνω.");
        }else if (details.get("cvv").length() != 3 || !details.get("cvv").matches("\\d+")) {
            view.showErrorMessage("Σφάλμα!", "Συμπληρώστε έγκυρο cvv.");
        }else if (userDAO.find(details.get("username"))!=null){
            view.showErrorMessage("Σφάλμα!","Υπάρχει ήδη λογαριασμός με αυτο το username \n Συμπληρώστε νέα στοιχεία!" );
        }else{
            Customer customer= new Customer(details.get("username"),details.get("name"),details.get("surname"),details.get("telephone"),
                    details.get("email"),details.get("password"), customerDAO.nextId(),details.get("cardNumber"),details.get("cardHolderName"),
                    details.get("cvv"));

            customerDAO.save(customer);
            userDAO.save(customer);

            view.showAccountCreatedMessage();
        }

    }
    public void onBack(){
        view.goBack();
    }
}
