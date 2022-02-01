package pl.edu.utp.kanbanboard.security.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pl.edu.utp.kanbanboard.security.model.JwtRequest;
import pl.edu.utp.kanbanboard.security.model.JwtResponse;
import pl.edu.utp.kanbanboard.security.utils.JwtTokenUtil;
import pl.edu.utp.kanbanboard.service.StudentService;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@CrossOrigin
public class JwtAuthenticationController {

    private JwtTokenUtil jwtTokenUtil;
    private PasswordEncoder passwordEncoder;
    private StudentService studentService;

    @PostMapping("/authenticate")
    public Mono<ResponseEntity<JwtResponse>> login(@RequestBody JwtRequest request) {
        return studentService.getByEmail(request.getUsername())
                .filter(userDetails ->
                        passwordEncoder.matches(request.getPassword(), userDetails.getPassword()))
                .map(userDetails ->
                        ResponseEntity.ok(new JwtResponse(jwtTokenUtil.generateToken(userDetails), userDetails.getUsername())))
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()));
    }

    @GetMapping(value = "/activate")
    public void canActivate() {
    }
}