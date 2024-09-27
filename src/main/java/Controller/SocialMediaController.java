package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import Model.Account;
import Model.Message;
import java.util.ArrayList;
import java.util.List;
import Service.AccountService;
import Service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */

     AccountService accountService;
     MessageService messageService;

    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::postRegistrationHandler);
        app.post("/login", this::postLoginHandler);
        app.post("/messages", this::postCreateMessageHandler);
        app.get("/messages", this::getMessageHandler);
        app.get("/messages/{message_id}", this::getMessageByIDHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIDHandler);
        app.patch("/messages/{message_id}", this::patchUpdateMesageByIDHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesFromAccountHandler);
        return app;
    }

    public SocialMediaController() {
        accountService = new AccountService();
        messageService = new MessageService();

    }

    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void postRegistrationHandler(Context context) {
        ObjectMapper objectMapper = new ObjectMapper();
        try { 
            Account account = objectMapper.readValue(context.body(), Account.class);
            Account account2 = accountService.createAccount(account);
            if (account2 == null) {
                System.out.println(1);
                context.status(400);
            } else {
                System.out.println(2);
                context.json(account2);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            context.status(400);
        }
    }

    private void postLoginHandler(Context context) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Account account = objectMapper.readValue(context.body(), Account.class);
            Account account2 = accountService.getLogin(account.getUsername(), account.getPassword());
            if (account2 == null) {
                context.status(401);
            } else {
                context.json(account2);
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            context.status(400);
        }
    }   

    private void postCreateMessageHandler(Context context) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Message message = objectMapper.readValue(context.body(), Message.class);
            Message message2 = messageService.serviceCreateMessage(message);

            if (message2 == null) {
                context.status(400);
            } else {
                context.json(message2);
                //context.status(200);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void getMessageHandler(Context context) {
        context.json(messageService.serviceGetAllMessages());
    }

    private void getMessageByIDHandler(Context context) {
        int id = Integer.parseInt(context.pathParam("message_id"));
        Message message = messageService.serviceFindMessageById(id);
        if (message == null) {
            context.status(200);
        } else {
            context.json(message);
        }
    }
        
    private void deleteMessageByIDHandler(Context context) {
        int id = Integer.parseInt(context.pathParam("message_id"));
        Message message = messageService.serviceDeleteMessage(id);
        if (message == null){
            context.status(200);
        } else {
        context.json(message);
        }
    }
    
    private void patchUpdateMesageByIDHandler(Context context) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            int id = Integer.parseInt(context.pathParam("message_id"));
            String new_message = objectMapper.readTree(context.body()).get("message_text").asText();
            Message message = messageService.serviceUpdateMessageById(id, new_message);
            if (message == null) {
                context.status(400);
            } else {
                context.json(message);
            }
        } catch (JsonProcessingException j) {
            j.printStackTrace();
            context.status(400);
        } 
    }

    private void getAllMessagesFromAccountHandler(Context context) {
        int id = Integer.parseInt(context.pathParam("account_id"));
        List<Message> messages = messageService.serviceGetAllMessagesByID(id);
        context.json(messages);
    }
   
}