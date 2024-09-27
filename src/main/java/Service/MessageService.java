package Service;
import DAO.MessageDAO;
import Model.Message;
import java.util.List;

public class MessageService {
    MessageDAO messageDAO;

    public MessageService() {
        messageDAO = new MessageDAO();
    }
    public MessageService(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }
    
    public Message serviceFindMessageById(int id) {
        return messageDAO.findMessageById(id);
    } 

    public Message serviceCreateMessage(Message message) {
        return messageDAO.createMessage(message);
    }

    public List<Message> serviceGetAllMessages(){
        return messageDAO.getAllMessages();
    }
    
    public Message serviceDeleteMessage(int id) {
        return messageDAO.deleteMessageByID(id);     
    }

    public Message serviceUpdateMessageById(int id, String msg) {
        return messageDAO.updateMessageByID(id, msg);
    }

    public List<Message> serviceGetAllMessagesByID(int id) {
        return messageDAO.getAllMessagesByAcctID(id);
    }
}