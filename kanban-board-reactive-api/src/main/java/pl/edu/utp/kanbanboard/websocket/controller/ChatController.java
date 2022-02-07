package pl.edu.utp.kanbanboard.websocket.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pl.edu.utp.kanbanboard.websocket.model.Message;
import pl.edu.utp.kanbanboard.websocket.service.ChatService;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping(value = "/api/chat", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/history/{projectId}")
    public Flux<Message> getHistory(@PathVariable String projectId) {
        return chatService.getHistory(projectId);
    }
}
