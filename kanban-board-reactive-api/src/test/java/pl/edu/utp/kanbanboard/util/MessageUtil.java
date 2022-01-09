package pl.edu.utp.kanbanboard.util;

import pl.edu.utp.kanbanboard.websocket.model.Message;

public class MessageUtil {
    public static Message getMessage1() {
        return new Message(StudentTestUtil.getStudent1(), "hello");
    }

    public static Message getMessage2() {
        return new Message(StudentTestUtil.getStudent2(), "hey");
    }

    public static Message getMessage3() {
        return new Message(StudentTestUtil.getStudent1(), "what's up?");
    }

    public static Message getMessage4() {
        return new Message(StudentTestUtil.getStudent1(), "test");
    }
}
