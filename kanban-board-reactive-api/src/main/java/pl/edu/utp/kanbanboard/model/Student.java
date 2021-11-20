package pl.edu.utp.kanbanboard.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Student {

    @Id
    private String studentId;

    private String firstName;

    private String lastName;

    private String indexNumber;

    private Boolean fullTime;

    private String email;

    private String password;

    public Student(String studentId, String firstName, String lastName, String indexNumber) {
        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.indexNumber = indexNumber;
    }

    public Student(String firstName, String lastName, String indexNumber,
                   Boolean fullTime, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.indexNumber = indexNumber;
        this.fullTime = fullTime;
        this.email = email;
        this.password = password;
    }

}
