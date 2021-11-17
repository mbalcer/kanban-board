package pl.edu.utp.pz1.security.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import pl.edu.utp.pz1.security.model.JwtRequest;
import pl.edu.utp.pz1.security.model.JwtResponse;
import pl.edu.utp.pz1.security.service.JwtUserDetailsService;
import pl.edu.utp.pz1.security.utils.JwtTokenUtil;

/**
 * Kontroler do uwierzytelniania zapytań HTTP
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class JwtAuthenticationController {

    private final Log logger = LogFactory.getLog(this.getClass());

    private AuthenticationManager authenticationManager;
    private JwtTokenUtil jwtTokenUtil;
    private JwtUserDetailsService userDetailsService;

    @Autowired
    public JwtAuthenticationController(AuthenticationManager authenticationManager,
                                       JwtTokenUtil jwtTokenUtil,
                                       JwtUserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Pobranie tokenu JWT po pomyślnym uwierzytelnieniu.
     *
     * @param request zapytanie HTTP
     * @return wygenerowany token lub błąd uwierzytelnienia
     */
    @PostMapping(value = "/authenticate")
    public ResponseEntity<JwtResponse> createAuthenticationToken(@RequestBody JwtRequest request) {
        UserDetails userDetails;
        try {
            authenticate(request.getUsername(), request.getPassword());
            userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        String token = jwtTokenUtil.generateToken(userDetails);
        String login = userDetails.getUsername();
        JwtResponse response = new JwtResponse(token, login);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Uwierzytelnienie użytkownika.
     *
     * @param username nazwa użytkownika (adres email)
     * @param password hasło
     * @throws AuthenticationException w przypadku błędu uwierzytelnienia
     */
    private void authenticate(String username, String password) throws AuthenticationException {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Email: " + username + " and given password are not correct");
        }
    }

    /**
     * Metoda wykorzystywana do weryfikacji użytkownika
     * na podstawie poprawności tokenu JWT.
     */
    @GetMapping(value = "/activate")
    public void canActivate() {
    }

}
