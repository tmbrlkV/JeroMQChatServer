package com.server.chat;

import com.chat.util.entity.User;
import com.chat.util.json.JsonObjectFactory;
import com.chat.util.json.JsonProtocol;
import org.zeromq.ZMQ;

class AuthorisationController {
    private final ZMQ.Socket databaseRequester;

    AuthorisationController(ZMQ.Socket databaseRequester) {
        this.databaseRequester = databaseRequester;
    }

    boolean authorization(String login, String password) throws Exception {

        User user = new User(login, password);
        String jsonString = JsonObjectFactory.getJsonString(new JsonProtocol<>("getUserByLoginPassword", user));
        databaseRequester.send(jsonString);

        String response = databaseRequester.recvStr();
        user = JsonObjectFactory.getObjectFromJson(response, User.class);

        assert user != null;
        return user.validation();
    }
}
