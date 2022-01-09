package pl.edu.utp.kanbanboard.websocket.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import pl.edu.utp.kanbanboard.websocket.handler.ChatWebSocketHandler;
import pl.edu.utp.kanbanboard.websocket.handler.TaskCreatedWebSocketHandler;
import pl.edu.utp.kanbanboard.websocket.handler.TaskEditedWebSocketHandler;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class WebSocketConfig {
    private final ChatWebSocketHandler chatWebSocketHandler;
    private final TaskCreatedWebSocketHandler taskCreatedWebSocketHandler;
    private final TaskEditedWebSocketHandler taskEditedWebSocketHandler;

    public WebSocketConfig(ChatWebSocketHandler chatWebSocketHandler, TaskCreatedWebSocketHandler taskCreatedWebSocketHandler, TaskEditedWebSocketHandler taskEditedWebSocketHandler) {
        this.chatWebSocketHandler = chatWebSocketHandler;
        this.taskCreatedWebSocketHandler = taskCreatedWebSocketHandler;
        this.taskEditedWebSocketHandler = taskEditedWebSocketHandler;
    }

    @Bean
    public HandlerMapping handlerMapping() {
        Map<String, Object> handlerMap = new HashMap<>();
        handlerMap.put("/app/chat/{projectId}", chatWebSocketHandler);
        handlerMap.put("/app/newTask/{projectId}", taskCreatedWebSocketHandler);
        handlerMap.put("/app/task/{taskId}", taskEditedWebSocketHandler);
        return new SimpleUrlHandlerMapping(handlerMap, 1);
    }

    @Bean
    public WebSocketHandlerAdapter webSocketHandlerAdapter() {
        return new WebSocketHandlerAdapter();
    }
}