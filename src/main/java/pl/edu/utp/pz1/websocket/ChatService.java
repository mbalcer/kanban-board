package pl.edu.utp.pz1.websocket;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ChatService {
    private Map<Long, List<Message>> historyOfMessages = new HashMap<>();

    public List<Message> getHistory(Long projectId) {
        return this.historyOfMessages.get(projectId);
    }

    public void addMessageToHistory(Long projectId, Message message) {
        Optional<List<Message>> messages = Optional.ofNullable(this.historyOfMessages.get(projectId));
        if (messages.isEmpty()) {
            this.historyOfMessages.put(projectId, new ArrayList<>(Arrays.asList(message)));
        } else {
            messages.get().add(message);
            this.historyOfMessages.put(projectId, messages.get());
        }
    }
}
