package DAO;
import java.util.List;
import java.util.ArrayList;
import java.sql.*;
import Util.ConnectionUtil;
import Model.Account;

public class AccountDAO {

    public AccountDAO() {}

    public Account findbyId(int userid) {     
        try {
            Connection connection = ConnectionUtil.getConnection();
            String sql = "SELECT * FROM Account WHERE account_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userid); 
            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()) {
                Account account = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
                return account;
            }

        } catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }


    public Account getLogin(String username, String password) {
        try {
            Connection connection = ConnectionUtil.getConnection();
            String sql = "SELECT * FROM Account WHERE username = ? AND password = ?;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                return new Account(rs.getInt(1), rs.getString(2), rs.getString(3));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Account> findAll() {
        List<Account> accountList = new ArrayList<Account>();
        try {
            Connection connection = ConnectionUtil.getConnection();
            String sql = "SELECT * FROM Account;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql); 
            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()) {
                Account account = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
                accountList.add(account);   
            }

        } catch(SQLException e){
            e.printStackTrace();
        }
        return accountList;

    }

    public Account createAccount(Account account) {
        try {
            if (account.getPassword().length() < 4 || findbyId(account.getAccount_id()) != null
            || account.getUsername().isBlank()) {
                System.out.println('a');
                return null;
            }
            Connection connection = ConnectionUtil.getConnection();
            String sql = "INSERT INTO Account (username, password) VALUES (?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS); 
            //preparedStatement.setInt(1, account.getAccount_id());
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());
            int i = preparedStatement.executeUpdate();
            if (i > 0) {
                ResultSet rs = preparedStatement.getGeneratedKeys();
                while (rs.next()) {
                    System.out.println('c');
                    return new Account(rs.getInt(1), account.getUsername(), account.getPassword());
                }
            }
        } catch(SQLException e){
            e.printStackTrace();
            System.out.println('d');
            return null;
        }
        System.out.println('e');
        return null;
    }

    public void delete(Account account) {
        try {
            Connection connection = ConnectionUtil.getConnection();
            String sql = "DELETE FROM Account WHERE account_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql); 
            preparedStatement.setInt(1, account.getAccount_id());
            preparedStatement.executeUpdate();

        } catch(SQLException e){
            e.printStackTrace();
        }
    }
}