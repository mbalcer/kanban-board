package pl.edu.utp.kanbanboard.security.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pl.edu.utp.kanbanboard.security.model.JwtRequest;
import pl.edu.utp.kanbanboard.security.model.JwtResponse;
import pl.edu.utp.kanbanboard.security.utils.JwtTokenUtil;
import pl.edu.utp.kanbanboard.service.StudentService;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class JwtAuthenticationController {

    private JwtTokenUtil jwtTokenUtil;
    private BCryptPasswordEncoder passwordEncoder;
    private StudentService studentService;

    @PostMapping("/login")
    public Mono<ResponseEntity<JwtResponse>> login(@RequestBody JwtRequest request) {
        return studentService.getByEmail(request.getUsername())
                .filter(userDetails ->
                        passwordEncoder.encode(request.getPassword()).equals(userDetails.getPassword()))
                .map(userDetails ->
                        ResponseEntity.ok(new JwtResponse(jwtTokenUtil.generateToken(userDetails), userDetails.getUsername())))
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()));
    }

}