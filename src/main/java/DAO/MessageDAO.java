package DAO;
import java.util.List;
import java.util.ArrayList;
import Util.ConnectionUtil;
import java.sql.*;
import Model.Message;

public class MessageDAO {
    
    public MessageDAO() {}

    AccountDAO accountDAO;

    public Message findMessageById(int id) {
        try {
            Connection connection = ConnectionUtil.getConnection();
            String sql = "SELECT * FROM Message WHERE message_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Message ans = new Message(rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"), 
                    rs.getLong("time_posted_epoch"));
                return ans;
            }
        } catch(SQLException e) {
            e.printStackTrace();
        } 
        return null;
    }

    public Message createMessage(Message message) {
        try {
            if (message.getMessage_text().isBlank() == true || 
                message.getMessage_text().length() > 255 ||
                getAllMessagesByAcctID(message.getPosted_by()).size() == 0) {
                    return null;
                }
            Connection connection = ConnectionUtil.getConnection();
            String sql = "INSERT INTO Message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            //ps.setInt(1, message.getMessage_id());
            ps.setInt(1, message.getPosted_by());
            ps.setString(2, message.getMessage_text());
            ps.setLong(3, message.getTime_posted_epoch());
            int i = ps.executeUpdate();
            if (i > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()){
                    int generatedId = rs.getInt(1);
                    System.out.println(2);
                    return new Message(generatedId, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
                }
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    } 

    public List<Message> getAllMessages() {
        List<Message> messagesList = new ArrayList<Message>();
        try {
            Connection connection = ConnectionUtil.getConnection();
            String sql = "SELECT * FROM Message";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"),
                    rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messagesList.add(message);
            }
            
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
        
        return messagesList;
    }

    public Message deleteMessageByID(int id) {
        Message msg = findMessageById(id);
        try {
            if (msg != null) {
                Connection connection = ConnectionUtil.getConnection();
                String sql = "DELETE FROM Message WHERE message_id = ?";
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, id);
                //ResultSet rs = ps.getGeneratedKeys();
                ps.executeUpdate();
            } else {
                return null;
            }

        } catch (SQLException e){
            e.printStackTrace();
        }
        return msg;
    }

    public Message updateMessageByID(int id, String msg) {
        try {
            if (msg.length() > 255 || msg.length() == 0 || msg == null
                || findMessageById(id) == null) {
                    return null;
                }
            Connection connection = ConnectionUtil.getConnection();
            String sql = "UPDATE Message SET message_text = ? WHERE message_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, msg);
            ps.setInt(2, id);
            ps.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
        return findMessageById(id);
    }
    
    public List<Message> getAllMessagesByAcctID(int account_id) {
        List<Message> messagesList = new ArrayList<Message>();
        try {
            Connection connection = ConnectionUtil.getConnection();
            String sql = "SELECT * FROM Message WHERE posted_by = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, account_id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"),
                    rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messagesList.add(message);
            }
            
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
        
        return messagesList;
    }
}
