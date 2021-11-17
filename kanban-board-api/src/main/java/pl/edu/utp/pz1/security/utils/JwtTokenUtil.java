package pl.edu.utp.pz1.security.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Klasa do zarządzania tokenami JWT
 */
@Component
public class JwtTokenUtil {

    /**
     * Czas ważności tokenu [ms]
     * Domyślnie: 30 minut
     */
    public static final long JWT_TOKEN_VALIDITY = 30 * 60 * 1000;

    /**
     * Klucz szyfrujący token
     */
    private String secret;

    public JwtTokenUtil(@Value("${jwt.secret}") String secret) {
        this.secret = secret;
    }

    public String getUsernameFromToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    public Date getExpirationDateFromToken(String token) {
        return getAllClaimsFromToken(token).getExpiration();
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    public Boolean isTokenExpired(String token) {
        Date expirationDate = getExpirationDateFromToken(token);
        return expirationDate.before(new Date());
    }

    /**
     * Metoda generująca token na podstawie danych użytkownika.
     *
     * @param userDetails dane użytkownika
     * @return wygenerowany token
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        String subject = userDetails.getUsername();
        Date expirationDate = new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * Metoda sprawdzająca poprawność tokenu na podstawie danych użytkownika.
     *
     * @param token       token do walidacji
     * @param userDetails dane użytkownika
     * @return true - jeśli token jest poprawny, false - w przeciwnym razie
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        String username = getUsernameFromToken(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

}
