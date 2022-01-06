package pl.edu.utp.kanbanboard.websocket;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ChatWebSocketHandler implements org.springframework.web.reactive.socket.WebSocketHandler {
    @Override
    public Mono<Void> handle(WebSocketSession session) {
        Flux<WebSocketMessage> stringFlux = session.receive()
                .map(WebSocketMessage::getPayloadAsText)
                .map(session::textMessage);
        return session.send(stringFlux);
    }
}
