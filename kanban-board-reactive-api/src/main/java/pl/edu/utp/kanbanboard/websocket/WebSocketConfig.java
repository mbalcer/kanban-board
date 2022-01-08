package pl.edu.utp.kanbanboard.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class WebSocketConfig {

    private final ChatWebSocketHandler chatWebSocketHandler;
    private final TaskCreatedWebSocketHandler taskCreatedWebSocketHandler;

    public WebSocketConfig(ChatWebSocketHandler chatWebSocketHandler, TaskCreatedWebSocketHandler taskCreatedWebSocketHandler) {
        this.chatWebSocketHandler = chatWebSocketHandler;
        this.taskCreatedWebSocketHandler = taskCreatedWebSocketHandler;
    }

    @Bean
    public HandlerMapping handlerMapping() {
        Map<String, Object> handlerMap = new HashMap<>();
        handlerMap.put("/app/chat/{projectId}", chatWebSocketHandler);
        handlerMap.put("/app/newTask/{projectId}", taskCreatedWebSocketHandler);

        return new SimpleUrlHandlerMapping(handlerMap, 1);
    }

    @Bean
    public WebSocketHandlerAdapter webSocketHandlerAdapter() {
        return new WebSocketHandlerAdapter();
    }
}