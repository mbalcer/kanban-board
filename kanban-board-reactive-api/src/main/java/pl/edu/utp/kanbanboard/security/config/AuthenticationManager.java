package pl.edu.utp.kanbanboard.security.config;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import pl.edu.utp.kanbanboard.security.utils.JwtTokenUtil;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private JwtTokenUtil jwtTokenUtil;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String authToken = authentication.getCredentials().toString();
        String username = jwtTokenUtil.getUsernameFromToken(authToken);
        return Mono.just(jwtTokenUtil.validateToken(authToken))
                .filter(valid -> valid)
                .switchIfEmpty(Mono.empty())
                .map(valid -> new UsernamePasswordAuthenticationToken(
                        username, null, null));
    }
}