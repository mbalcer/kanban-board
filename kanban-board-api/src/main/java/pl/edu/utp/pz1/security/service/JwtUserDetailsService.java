package pl.edu.utp.pz1.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.edu.utp.pz1.model.Student;
import pl.edu.utp.pz1.repository.StudentRepository;

/**
 * Serwis dla obsługi danych użytkownika,
 * wykorzystywany w ramach zabezpieczeń REST API.
 */
@Service
public class JwtUserDetailsService implements UserDetailsService {

    private StudentRepository studentRepository;

    @Autowired
    public JwtUserDetailsService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    /**
     * Pobranie użytkownika na podstawie jego nazwy (adresu email)
     *
     * @param username adres email użytkownika
     * @return znaleziony użytkownik
     * @throws UsernameNotFoundException w przypadku braku użytkownika
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Student student = studentRepository.findByEmail(username).orElse(null);
        if (student != null) {
            return student;
        } else {
            throw new UsernameNotFoundException("Active user with email: " + username + " not found");
        }
    }

}
