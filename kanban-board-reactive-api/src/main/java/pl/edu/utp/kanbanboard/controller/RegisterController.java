package pl.edu.utp.kanbanboard.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.utp.kanbanboard.model.RegisterEntry;
import pl.edu.utp.kanbanboard.service.RegisterService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@RestController
@RequestMapping(value = "/api/register", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin
public class RegisterController {
    private final RegisterService registerService;

    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @GetMapping("/all")
    public Flux<RegisterEntry> getAllRegisters() {
        return registerService.all();
    }

    @GetMapping("/project/{projectId}")
    public Flux<RegisterEntry> getAllTasksByProjectId(@PathVariable String projectId) {
        return registerService.allByProjectId(projectId);
    }

    @GetMapping("/date/{date}")
    public Flux<RegisterEntry> getAllTasksByProjectId(@PathVariable LocalDate date) {
        return registerService.allByDate(date);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<RegisterEntry>> getRegisterById(@PathVariable String id) {
        return registerService.get(id)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @GetMapping("/project-date/{projectId}/{date}")
    public Mono<ResponseEntity<RegisterEntry>> getByProjectIdAndDate(@PathVariable String projectId,
                                                                     @PathVariable LocalDate date) {
        return registerService.getByProjectIdAndDate(projectId, date)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @GetMapping("/update")
    public Flux<RegisterEntry> updateAll() {
        return registerService.updateAll();
    }

}
