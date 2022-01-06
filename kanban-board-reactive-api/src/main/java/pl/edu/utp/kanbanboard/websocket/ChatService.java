package pl.edu.utp.kanbanboard.websocket;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class ChatService {
    private Map<String, Flux<Message>> historyOfMessages = new HashMap<>();

    public Flux<Message> getHistory(String projectId) {
        return this.historyOfMessages.get(projectId);
    }

    public void addMessageToHistory(String projectId, Message message) {
        Optional<Flux<Message>> messages = Optional.ofNullable(this.historyOfMessages.get(projectId));
        if (messages.isEmpty()) {
            this.historyOfMessages.put(projectId, Flux.just(message));
        } else {
            Flux<Message> messageFlux = Flux.concat(messages.get(), Flux.just(message));
            this.historyOfMessages.put(projectId, messageFlux);
        }
    }
}
