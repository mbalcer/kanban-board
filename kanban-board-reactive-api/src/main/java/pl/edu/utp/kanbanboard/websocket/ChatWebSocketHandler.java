package pl.edu.utp.kanbanboard.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.reactive.socket.adapter.ReactorNettyWebSocketSession;
import org.springframework.web.util.UriTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Map;

@Service
public class ChatWebSocketHandler implements WebSocketHandler {
    private ObjectMapper objectMapper;
    private ChatService chatService;

    public ChatWebSocketHandler(ObjectMapper objectMapper, ChatService chatService) {
        this.objectMapper = objectMapper;
        this.chatService = chatService;
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        Flux<WebSocketMessage> messages = session.receive()
                .map(WebSocketMessage::getPayloadAsText)
                .map(this::toMessage)
                .doOnNext(message -> {
                    UriTemplate template = new UriTemplate("/app/chat/{projectId}");
                    Map<String, String> parameters = template.match(getConnectionUri(session).getPath());
                    String projectId = parameters.get("projectId");

                    chatService.addMessageToHistory(projectId, message);
                })
                .flatMap(this::toString)
                .map(session::textMessage);
        return session.send(messages);
    }

    private Message toMessage(String json) {
        try {
            return objectMapper.readValue(json, Message.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Invalid JSON:" + json, e);
        }
    }

    private Mono<String> toString(Message message) {
        try {
            return Mono.just(objectMapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            return Mono.error(e);
        }
    }

    private URI getConnectionUri(WebSocketSession session) {
        ReactorNettyWebSocketSession nettySession = (ReactorNettyWebSocketSession) session;
        return nettySession.getHandshakeInfo().getUri();
    }
}
