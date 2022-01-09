package pl.edu.utp.kanbanboard.websocket.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.reactive.socket.adapter.ReactorNettyWebSocketSession;
import org.springframework.web.util.UriTemplate;
import pl.edu.utp.kanbanboard.event.TaskCreatedEventPublisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Map;

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
                .filter(task -> {
                    UriTemplate template = new UriTemplate("/app/newTask/{projectId}");
                    Map<String, String> parameters = template.match(getConnectionUri(session).getPath());
                    String projectId = parameters.get("projectId");

                    return task.getDescription().equals(projectId); // TODO: change to task.getProject().getId().equals(projectId) when a project relationship is added to the task class
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
