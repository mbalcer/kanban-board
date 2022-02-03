package pl.edu.utp.kanbanboard.service.impl;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.edu.utp.kanbanboard.model.RegisterEntry;
import pl.edu.utp.kanbanboard.model.Task;
import pl.edu.utp.kanbanboard.model.TaskState;
import pl.edu.utp.kanbanboard.repository.ProjectRepository;
import pl.edu.utp.kanbanboard.repository.RegisterRepository;
import pl.edu.utp.kanbanboard.repository.TaskRepository;
import pl.edu.utp.kanbanboard.service.RegisterService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class RegisterServiceImpl implements RegisterService {
    private final RegisterRepository registerRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    public RegisterServiceImpl(RegisterRepository registerRepository,
                               ProjectRepository projectRepository,
                               TaskRepository taskRepository) {
        this.registerRepository = registerRepository;
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public Flux<RegisterEntry> all() {
        return this.registerRepository.findAll();
    }

    @Override
    public Flux<RegisterEntry> allByProjectId(String projectId) {
        return this.registerRepository.findAllByProject(projectId);
    }

    @Override
    public Flux<RegisterEntry> allByDate(LocalDate date) {
        return this.registerRepository.findAllByDate(date);
    }

    @Override
    public Mono<RegisterEntry> get(String id) {
        return this.registerRepository.findById(id);
    }

    @Override
    public Mono<RegisterEntry> getByProjectIdAndDate(String projectId, LocalDate date) {
        return this.registerRepository.findFirstByProjectAndDate(projectId, date);
    }

    @Override
    @Scheduled(cron = "0 0 0 * * ?") // automatically invoked every day at midnight UTC
    public Flux<RegisterEntry> updateAll() {
        return this.projectRepository.findAll()
                .flatMap(project -> {
                    LocalDate now = LocalDate.now();
                    // delete entry with the same date if exists
                    project.getFlowRegister().forEach(entryId ->
                            registerRepository.findById(entryId)
                                    .filter(entry -> entry.getDate().isEqual(now))
                                    .doOnNext(this.registerRepository::delete)
                                    .doOnSuccess(entry -> project.getFlowRegister().remove(entry.getEntryId())));
                    // initialize map for each task state with 0 values
                    Map<TaskState, Integer> registerFlow = new HashMap<>();
                    for (TaskState taskState : TaskState.values()) {
                        registerFlow.put(taskState, 0);
                    }
                    // update map when task with given state found
                    project.getTasks().forEach(taskId ->
                            taskRepository.findById(taskId)
                                    .map(Task::getState)
                                    .doOnNext(state -> registerFlow.put(state, registerFlow.get(state) + 1)));
                    // create register entry and save it
                    RegisterEntry entry = new RegisterEntry();
                    entry.setEntryId(UUID.randomUUID().toString());
                    entry.setDate(now);
                    entry.setFlow(registerFlow);
                    entry.setProject(project.getProjectId());
                    return this.registerRepository.save(entry)
                            .doOnSuccess(next -> {
                                project.getFlowRegister().add(entry.getEntryId());
                                this.projectRepository.save(project);
                            });
                });
    }

}
