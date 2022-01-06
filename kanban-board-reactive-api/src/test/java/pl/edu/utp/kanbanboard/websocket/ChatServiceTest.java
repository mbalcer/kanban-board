package pl.edu.utp.kanbanboard.websocket;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import pl.edu.utp.kanbanboard.util.MessageUtil;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.HashMap;
import java.util.Map;

@ExtendWith(SpringExtension.class)
public class ChatServiceTest {
    public static final String PROJECT_1 = "123";
    public static final String PROJECT_2 = "321";
    public static final String PROJECT_3 = "987";

    @InjectMocks
    private ChatService chatService;

    private Map<String, Flux<Message>> historyOfMessages = new HashMap<>();

    @BeforeEach
    void setUp() {
        historyOfMessages.put(PROJECT_1, Flux.just(MessageUtil.getMessage1(), MessageUtil.getMessage2(), MessageUtil.getMessage3()));
        historyOfMessages.put(PROJECT_2, Flux.just(MessageUtil.getMessage1(), MessageUtil.getMessage4()));

        ReflectionTestUtils.setField(chatService, "historyOfMessages", historyOfMessages);
    }

    @Test
    public void testGetHistory() {
        Flux<Message> result = chatService.getHistory(PROJECT_1);

        StepVerifier
                .create(result)
                .expectNext(MessageUtil.getMessage1())
                .expectNext(MessageUtil.getMessage2())
                .expectNext(MessageUtil.getMessage3())
                .verifyComplete();
    }

    @Test
    public void testAddMessageToHistory_whenProjectIsExists() {
        chatService.addMessageToHistory(PROJECT_2, MessageUtil.getMessage2());

        Flux<Message> result = chatService.getHistory(PROJECT_2);

        StepVerifier
                .create(result)
                .expectNext(MessageUtil.getMessage1())
                .expectNext(MessageUtil.getMessage4())
                .expectNext(MessageUtil.getMessage2())
                .verifyComplete();
    }

    @Test
    public void testAddMessageToHistory_whenProjectIsNotExists() {
        chatService.addMessageToHistory(PROJECT_3, MessageUtil.getMessage4());

        Flux<Message> result = chatService.getHistory(PROJECT_3);

        StepVerifier
                .create(result)
                .expectNext(MessageUtil.getMessage4())
                .verifyComplete();
    }
}
