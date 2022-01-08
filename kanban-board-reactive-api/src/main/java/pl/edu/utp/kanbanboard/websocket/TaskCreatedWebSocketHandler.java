package pl.edu.utp.kanbanboard.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import pl.edu.utp.kanbanboard.event.TaskCreatedEventPublisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TaskCreatedWebSocketHandler implements WebSocketHandler {
    private ObjectMapper objectMapper;
    private TaskCreatedEventPublisher eventPublisher;

    public TaskCreatedWebSocketHandler(ObjectMapper objectMapper, TaskCreatedEventPublisher taskCreatedEventPublisher) {
        this.objectMapper = objectMapper;
        this.eventPublisher = taskCreatedEventPublisher;
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        Flux<WebSocketMessage> messages = Flux.create(eventPublisher)
                .share()
                .map(this::toString)
                .map(session::textMessage);
        return session.send(messages);
    }

    public String toString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
