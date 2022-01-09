package pl.edu.utp.kanbanboard.websocket.service;

import pl.edu.utp.kanbanboard.websocket.model.Message;
import reactor.core.publisher.Flux;

public interface ChatService {
    Flux<Message> getHistory(String projectId);

    void addMessageToHistory(String projectId, Message message);
}
