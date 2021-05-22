package pl.edu.utp.pz1.websocket;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
public class WebSocketController {

    private ChatService chatService;

    public WebSocketController(ChatService chatService) {
        this.chatService = chatService;
    }

    @MessageMapping("/chat/{projectId}")
    @SendTo("/project/{projectId}")
    public Message getMessage(@DestinationVariable Long projectId, Message message) {
        chatService.addMessageToHistory(projectId, message);
        return message;
    }

    @GetMapping("/chat/history/{projectId}")
    public List<Message> getHistory(@PathVariable Long projectId) {
        return chatService.getHistory(projectId);
    }
}
