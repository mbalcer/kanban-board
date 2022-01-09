package pl.edu.utp.kanbanboard.event;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import reactor.core.publisher.FluxSink;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

@Component
public class TaskEditedEventPublisher implements ApplicationListener<TaskEditedEvent>, Consumer<FluxSink<TaskEditedEvent>> {
    private final Executor executor;
    private final BlockingQueue<TaskEditedEvent> queue = new LinkedBlockingQueue<>();

    public TaskEditedEventPublisher(Executor executor) {
        this.executor = executor;
    }

    @Override
    public void accept(FluxSink<TaskEditedEvent> sink) {
        this.executor.execute(() -> {
            while (true)
                try {
                    TaskEditedEvent event = queue.take();
                    sink.next(event);
                } catch (InterruptedException e) {
                    ReflectionUtils.rethrowRuntimeException(e);
                }
        });
    }

    @Override
    public void onApplicationEvent(TaskEditedEvent event) {
        this.queue.offer(event);
    }
}
