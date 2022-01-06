package pl.edu.utp.kanbanboard.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;

import java.util.Map;

@Slf4j
@Configuration
public class WebSocketConfig {

    private final ChatWebSocketHandler chatWebSocketHandler;

    public WebSocketConfig(ChatWebSocketHandler chatWebSocketHandler) {
        this.chatWebSocketHandler = chatWebSocketHandler;
    }

    @Bean
    public HandlerMapping handlerMapping() {
        Map<String, ChatWebSocketHandler> handlerMap = Map.of(
                "/chat", chatWebSocketHandler
        );
        return new SimpleUrlHandlerMapping(handlerMap, 1);
    }
}