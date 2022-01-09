package pl.edu.utp.kanbanboard.websocket.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.reactive.socket.adapter.ReactorNettyWebSocketSession;
import org.springframework.web.util.UriTemplate;
import pl.edu.utp.kanbanboard.event.TaskEditedEventPublisher;
import pl.edu.utp.kanbanboard.model.Task;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Map;

@Service
public class TaskEditedWebSocketHandler implements WebSocketHandler {
    private final ObjectMapper objectMapper;
    private final TaskEditedEventPublisher taskEditedEventPublisher;

    public TaskEditedWebSocketHandler(ObjectMapper objectMapper, TaskEditedEventPublisher taskEditedEventPublisher) {
        this.objectMapper = objectMapper;
        this.taskEditedEventPublisher = taskEditedEventPublisher;
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        Flux<WebSocketMessage> messages = Flux.create(taskEditedEventPublisher)
                .share()
                .filter(event -> {
                    UriTemplate template = new UriTemplate("/app/task/{taskId}");
                    Map<String, String> parameters = template.match(getConnectionUri(session).getPath());
                    String taskId = parameters.get("taskId");

                    Task source = (Task) event.getSource();
                    return source.getTaskId().equals(taskId);
                })
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

    private URI getConnectionUri(WebSocketSession session) {
        ReactorNettyWebSocketSession nettySession = (ReactorNettyWebSocketSession) session;
        return nettySession.getHandshakeInfo().getUri();
    }
}
