package Service;
import DAO.AccountDAO;
import Model.Account;
import java.util.List;


public class AccountService {

    AccountDAO accountDAO;

    public AccountService() {
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public Account findByID(int id) {
        return accountDAO.findbyId(id);
    }

    public Account getLogin(String username, String password) {
        return accountDAO.getLogin(username, password);
    }
    
    public Account createAccount(Account account) {
        return accountDAO.createAccount(account);
    }

    public List<Account> findAll() {
        return accountDAO.findAll();
    }

    public void delete(Account account) {
        accountDAO.delete(account);
    }
}