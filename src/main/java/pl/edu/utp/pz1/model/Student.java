package pl.edu.utp.pz1.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "student")
@NoArgsConstructor
@Data
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer studentId;

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(nullable = false, length = 100)
    private String lastName;

    @Column(nullable = false, unique = true, length = 20)
    private String indexNumber;

    @Column(length = 50)
    private String email;

    @Column(nullable = false)
    private Boolean fullTime;

    @ManyToMany(mappedBy = "students")
    @JsonIgnoreProperties({"student"})
    private Set<Project> projects;

    public Student(String firstName, String lastName, String indexNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.indexNumber = indexNumber;
    }

    public Student(String firstName, String lastName, String indexNumber, String email, Boolean fullTime) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.indexNumber = indexNumber;
        this.email = email;
        this.fullTime = fullTime;
    }

}
