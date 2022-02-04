package pl.edu.utp.kanbanboard.service.impl;

import org.springframework.stereotype.Service;
import pl.edu.utp.kanbanboard.model.Project;
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
    public Mono<Project> update(String projectId) {
        LocalDate now = LocalDate.now();
        return this.projectRepository.findById(projectId)
                .doOnNext(project -> {
                    // delete entry with the same date if exists
                    this.registerRepository.findAllByProject(projectId)
                            .filter(entry -> entry.getDate().isEqual(now))
                            .doOnNext(entry -> {
                                project.getFlowRegister().remove(entry.getEntryId());
                                this.registerRepository.delete(entry).subscribe();
                            }).doOnComplete(() -> this.projectRepository.save(project))
                            .subscribe();
                })
                .doOnNext(project -> {
                    // initialize map for each task state with 0 values
                    Map<TaskState, Integer> registerFlow = new HashMap<>();
                    for (TaskState taskState : TaskState.values()) {
                        registerFlow.put(taskState, 0);
                    }
                    this.taskRepository.findAllByProject(projectId)
                            // update map when task with given state found
                            .map(Task::getState)
                            .doOnNext(state -> registerFlow.put(state, registerFlow.get(state) + 1))
                            .doOnComplete(() -> {
                                // create register entry and save it
                                RegisterEntry entry = new RegisterEntry();
                                entry.setEntryId(UUID.randomUUID().toString());
                                entry.setDate(now);
                                entry.setFlow(registerFlow);
                                entry.setProject(project.getProjectId());
                                this.registerRepository.save(entry)
                                        .doOnSuccess(next -> {
                                            project.getFlowRegister().add(entry.getEntryId());
                                            this.projectRepository.save(project).subscribe();
                                        }).subscribe();
                            }).subscribe();
                });
    }

    // TODO: update all projects

}
