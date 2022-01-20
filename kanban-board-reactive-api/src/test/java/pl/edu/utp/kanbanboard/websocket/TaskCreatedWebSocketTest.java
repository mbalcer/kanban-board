package pl.edu.utp.kanbanboard.websocket;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.reactivestreams.Publisher;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import pl.edu.utp.kanbanboard.model.Task;
import pl.edu.utp.kanbanboard.util.TaskUtil;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.atomic.AtomicLong;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaskCreatedWebSocketTest {
    @LocalServerPort
    private String port;

    private final WebSocketClient socketClient = new ReactorNettyWebSocketClient();

    private final WebClient webClient = WebClient.builder().build();

    @Test
    @WithMockUser
    public void testTaskCreatedEvent() throws Exception {
        int count = 10;
        AtomicLong counter = new AtomicLong();
        URI uri = getUrl("/app/newTask/123");

        socketClient.execute(uri, (WebSocketSession session) -> {
            Mono<WebSocketMessage> out = Mono.just(session.textMessage("test"));
            Flux<String> in = session
                    .receive()
                    .map(WebSocketMessage::getPayloadAsText);

            return session
                    .send(out)
                    .thenMany(in)
                    .doOnNext(str -> counter.incrementAndGet())
                    .then();
        }).subscribe();

        Flux.<Task>generate(sink -> sink.next(TaskUtil.generateTask()))
                .take(count)
                .flatMap(this::write)
                .blockLast();

        Thread.sleep(1000);

        Assertions.assertThat(counter.get()).isEqualTo(count);
    }

    private Publisher<Task> write(Task p) {
        return this.webClient
                    .post()
                    .uri("http://localhost:" + this.port + "/api/task")
                    .body(BodyInserters.fromValue(p))
                    .retrieve()
                    .bodyToMono(String.class)
                    .thenReturn(p);
    }

    protected URI getUrl(String path) throws URISyntaxException {
        return new URI("ws://localhost:" + this.port + path);
    }
}
