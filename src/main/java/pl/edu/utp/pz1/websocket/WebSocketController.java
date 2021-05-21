package pl.edu.utp.pz1.websocket;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class WebSocketController {
    @MessageMapping("/chat/{projectId}")
    @SendTo("/project/{projectId}")
    public Message getMessage(@DestinationVariable Long projectId, Message message) {
        return message;
    }
}
