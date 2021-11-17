package pl.edu.utp.pz1.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.utp.pz1.model.Student;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

    Optional<Student> findByIndexNumber(String indexNumber);

    Optional<Student> findByEmail(String email);

    Page<Student> findByIndexNumberStartsWith(String indexNumber, Pageable pageable);

    Page<Student> findByLastNameStartsWithIgnoreCase(String lastName, Pageable pageable);

}
