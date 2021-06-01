package pl.edu.utp.pz1.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.edu.utp.pz1.exception.EmailAlreadyUsedException;
import pl.edu.utp.pz1.exception.IndexAlreadyUsedException;
import pl.edu.utp.pz1.exception.PasswordNotCorrectException;
import pl.edu.utp.pz1.exception.StudentNotFoundException;
import pl.edu.utp.pz1.model.Student;
import pl.edu.utp.pz1.repository.StudentRepository;
import pl.edu.utp.pz1.service.impl.StudentServiceImpl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @InjectMocks
    private StudentServiceImpl studentService;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    public void findById() {
        Student given = new Student();
        given.setStudentId(3);
        given.setEmail("student@test.pl");

        when(studentRepository.findById(given.getStudentId())).thenReturn(Optional.of(given));
        Student actual = studentService.findById(given.getStudentId()).orElse(null);

        assertNotNull(actual);
        assertEquals(given.getEmail(), actual.getEmail());
        verify(studentRepository, times(1)).findById(given.getStudentId());
        verifyNoMoreInteractions(studentRepository);
    }

    @Test
    public void findByEmail() {
        Student given = new Student();
        given.setStudentId(3);
        given.setEmail("student@test.pl");

        when(studentRepository.findByEmail(given.getEmail())).thenReturn(Optional.of(given));
        Student actual = studentService.findByEmail(given.getEmail()).orElse(null);

        assertNotNull(actual);
        assertEquals(given.getStudentId(), actual.getStudentId());
        verify(studentRepository, times(1)).findByEmail(given.getEmail());
        verifyNoMoreInteractions(studentRepository);
    }

    @Test
    public void getStudents() {
        int page = 5;
        int size = 15;
        Pageable pageable = PageRequest.of(page, size);

        when(studentRepository.findAll(pageable))
                .thenReturn(new PageImpl<>(Collections.emptyList(), pageable, 0));
        Page<Student> studentPage = studentService.getStudents(pageable);

        assertEquals(page, studentPage.getPageable().getPageNumber());
        assertEquals(size, studentPage.getPageable().getPageSize());
        verify(studentRepository, times(1)).findAll(pageable);
        verifyNoMoreInteractions(studentRepository);
    }

    @Test
    public void findAll() {
        Student student1 = new Student();
        student1.setStudentId(3);
        student1.setEmail("student1@test.pl");

        Student student2 = new Student();
        student2.setStudentId(4);
        student2.setEmail("student2@test.pl");

        List<Student> given = Arrays.asList(student1, student2);

        when(studentRepository.findAll()).thenReturn(given);
        List<Student> actual = studentService.findAll();

        assertEquals(given.size(), actual.size());
        assertThat(actual, containsInAnyOrder(student1, student2));
        verify(studentRepository, times(1)).findAll();
        verifyNoMoreInteractions(studentRepository);
    }

    @Test
    public void createStudent_whenSuccess() {
        Student given = new Student("FirstName", "LastName", "112233",
                true, "student@test.pl", "Qwerty.1");

        when(studentRepository.findByEmail(given.getEmail())).thenReturn(Optional.empty());
        when(studentRepository.findByIndexNumber(given.getIndexNumber())).thenReturn(Optional.empty());
        when(studentRepository.save(given)).thenReturn(given);
        when(passwordEncoder.encode(given.getPassword())).thenReturn("#Qwerty.1");

        Student actual = studentService.create(given);

        assertEquals(given.getEmail(), actual.getEmail());
        assertEquals("#Qwerty.1", actual.getPassword());
        verify(studentRepository, times(1)).findByEmail(given.getEmail());
        verify(studentRepository, times(1)).findByIndexNumber(given.getIndexNumber());
        verify(studentRepository, times(1)).save(given);
        verify(passwordEncoder, times(1)).encode(any());
        verifyNoMoreInteractions(studentRepository);
        verifyNoMoreInteractions(passwordEncoder);
    }

    @Test
    public void createStudent_whenEmailAlreadyUsed() {
        Student given = new Student("FirstName", "LastName", "112233",
                true, "student@test.pl", "Qwerty.1");

        when(studentRepository.findByEmail(given.getEmail())).thenReturn(Optional.of(new Student()));

        assertThrows(EmailAlreadyUsedException.class, () -> studentService.create(given));

        assertEquals("Qwerty.1", given.getPassword());
        verify(studentRepository, times(1)).findByEmail(given.getEmail());
        verify(studentRepository, times(0)).findByIndexNumber(given.getIndexNumber());
        verify(studentRepository, times(0)).save(given);
        verify(passwordEncoder, times(0)).encode(any());
        verifyNoMoreInteractions(studentRepository);
        verifyNoMoreInteractions(passwordEncoder);
    }

    @Test
    public void createStudent_whenIndexAlreadyUsed() {
        Student given = new Student("FirstName", "LastName", "112233",
                true, "student@test.pl", "Qwerty.1");

        when(studentRepository.findByEmail(given.getEmail())).thenReturn(Optional.empty());
        when(studentRepository.findByIndexNumber(given.getIndexNumber())).thenReturn(Optional.of(new Student()));

        assertThrows(IndexAlreadyUsedException.class, () -> studentService.create(given));

        assertEquals("Qwerty.1", given.getPassword());
        verify(studentRepository, times(1)).findByEmail(given.getEmail());
        verify(studentRepository, times(1)).findByIndexNumber(given.getIndexNumber());
        verify(studentRepository, times(0)).save(given);
        verify(passwordEncoder, times(0)).encode(any());
        verifyNoMoreInteractions(studentRepository);
        verifyNoMoreInteractions(passwordEncoder);
    }

    @Test
    public void updateStudent_whenSuccess() {
        Student given = new Student(null, null, "000000",
                null, "student@test.pl", null);

        Student newStudent = new Student("FirstName", "LastName", "112233",
                true, "newStudent@test.pl", "Qwerty.1");

        when(studentRepository.findById(given.getStudentId())).thenReturn(Optional.of(given));
        when(studentRepository.findByEmail(newStudent.getEmail())).thenReturn(Optional.empty());
        when(studentRepository.findByIndexNumber(newStudent.getIndexNumber())).thenReturn(Optional.empty());
        when(studentRepository.save(given)).thenReturn(given);

        Student actual = studentService.update(given.getStudentId(), newStudent);

        assertEquals(newStudent.getFirstName(), actual.getFirstName());
        assertEquals(newStudent.getLastName(), actual.getLastName());
        assertEquals(newStudent.getIndexNumber(), actual.getIndexNumber());
        assertEquals(newStudent.getFullTime(), actual.getFullTime());
        assertEquals(newStudent.getEmail(), actual.getEmail());
        assertEquals(given.getPassword(), actual.getPassword());
        verify(studentRepository, times(1)).findById(given.getStudentId());
        verify(studentRepository, times(1)).findByEmail(newStudent.getEmail());
        verify(studentRepository, times(1)).findByIndexNumber(newStudent.getIndexNumber());
        verify(studentRepository, times(1)).save(given);
        verifyNoMoreInteractions(studentRepository);
    }

    @Test
    public void updateStudent_whenStudentNotFound() {
        Student given = new Student(null, null, "000000",
                null, "student@test.pl", null);

        Student newStudent = new Student("FirstName", "LastName", "112233",
                true, "newStudent@test.pl", "Qwerty.1");

        when(studentRepository.findById(given.getStudentId())).thenReturn(Optional.empty());
        assertThrows(StudentNotFoundException.class, () -> studentService.update(given.getStudentId(), newStudent));

        assertNotEquals(newStudent.getFirstName(), given.getFirstName());
        assertNotEquals(newStudent.getLastName(), given.getLastName());
        assertNotEquals(newStudent.getIndexNumber(), given.getIndexNumber());
        assertNotEquals(newStudent.getFullTime(), given.getFullTime());
        assertNotEquals(newStudent.getEmail(), given.getEmail());
        assertNotEquals(newStudent.getPassword(), given.getPassword());
        verify(studentRepository, times(1)).findById(given.getStudentId());
        verify(studentRepository, times(0)).findByEmail(newStudent.getEmail());
        verify(studentRepository, times(0)).findByIndexNumber(newStudent.getIndexNumber());
        verify(studentRepository, times(0)).save(any());
        verifyNoMoreInteractions(studentRepository);
    }

    @Test
    public void updateStudent_whenEmailAlreadyUsed() {
        Student given = new Student(null, null, "000000",
                null, "student@test.pl", null);

        Student newStudent = new Student("FirstName", "LastName", "112233",
                true, "newStudent@test.pl", "Qwerty.1");

        when(studentRepository.findById(given.getStudentId())).thenReturn(Optional.of(given));
        when(studentRepository.findByEmail(newStudent.getEmail())).thenReturn(Optional.of(new Student()));

        assertThrows(EmailAlreadyUsedException.class, () -> studentService.update(given.getStudentId(), newStudent));

        assertNotEquals(newStudent.getFirstName(), given.getFirstName());
        assertNotEquals(newStudent.getLastName(), given.getLastName());
        assertNotEquals(newStudent.getIndexNumber(), given.getIndexNumber());
        assertNotEquals(newStudent.getFullTime(), given.getFullTime());
        assertNotEquals(newStudent.getEmail(), given.getEmail());
        assertNotEquals(newStudent.getPassword(), given.getPassword());
        verify(studentRepository, times(1)).findById(given.getStudentId());
        verify(studentRepository, times(1)).findByEmail(newStudent.getEmail());
        verify(studentRepository, times(0)).findByIndexNumber(newStudent.getIndexNumber());
        verify(studentRepository, times(0)).save(any());
        verifyNoMoreInteractions(studentRepository);
    }

    @Test
    public void updateStudent_whenIndexAlreadyUsed() {
        Student given = new Student(null, null, "000000",
                null, "student@test.pl", null);

        Student newStudent = new Student("FirstName", "LastName", "112233",
                true, "newStudent@test.pl", "Qwerty.1");

        when(studentRepository.findById(given.getStudentId())).thenReturn(Optional.of(given));
        when(studentRepository.findByEmail(newStudent.getEmail())).thenReturn(Optional.empty());
        when(studentRepository.findByIndexNumber(newStudent.getIndexNumber())).thenReturn(Optional.of(new Student()));

        assertThrows(IndexAlreadyUsedException.class, () -> studentService.update(given.getStudentId(), newStudent));

        assertNotEquals(newStudent.getFirstName(), given.getFirstName());
        assertNotEquals(newStudent.getLastName(), given.getLastName());
        assertNotEquals(newStudent.getIndexNumber(), given.getIndexNumber());
        assertNotEquals(newStudent.getFullTime(), given.getFullTime());
        assertNotEquals(newStudent.getEmail(), given.getEmail());
        assertNotEquals(newStudent.getPassword(), given.getPassword());
        verify(studentRepository, times(1)).findById(given.getStudentId());
        verify(studentRepository, times(1)).findByEmail(newStudent.getEmail());
        verify(studentRepository, times(1)).findByIndexNumber(newStudent.getIndexNumber());
        verify(studentRepository, times(0)).save(any());
        verifyNoMoreInteractions(studentRepository);
    }

    @Test
    public void updatePassword_whenCorrect() {
        Student given = new Student("FirstName", "LastName", "112233",
                true, "student@test.pl", "Qwerty.1");
        given.setStudentId(2);
        String currentPassword = "Qwerty.1";
        String newPassword = "Qwerty.2";

        when(studentRepository.findById(given.getStudentId())).thenReturn(Optional.of(given));
        when(studentRepository.save(given)).thenReturn(given);
        when(passwordEncoder.matches(currentPassword, given.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(newPassword)).thenReturn("#" + newPassword);

        Student actual = studentService.updatePassword(given.getStudentId(), currentPassword, newPassword);

        assertEquals(given.getEmail(), actual.getEmail());
        assertEquals("#" + newPassword, actual.getPassword());
        verify(studentRepository, times(1)).findById(given.getStudentId());
        verify(studentRepository, times(1)).save(given);
        verify(passwordEncoder, times(1)).matches(eq(currentPassword), anyString());
        verify(passwordEncoder, times(1)).encode(newPassword);
        verifyNoMoreInteractions(studentRepository);
        verifyNoMoreInteractions(passwordEncoder);
    }

    @Test
    public void updatePassword_whenStudentNotFound() {
        Student given = new Student("FirstName", "LastName", "112233",
                true, "student@test.pl", "Qwerty.1");
        given.setStudentId(2);
        String currentPassword = "Qwerty.1";
        String newPassword = "Qwerty.2";

        when(studentRepository.findById(given.getStudentId())).thenReturn(Optional.empty());
        assertThrows(StudentNotFoundException.class, () ->
                studentService.updatePassword(given.getStudentId(), currentPassword, newPassword));

        assertNotEquals(newPassword, given.getPassword());
        verify(studentRepository, times(1)).findById(given.getStudentId());
        verify(studentRepository, times(0)).save(given);
        verify(passwordEncoder, times(0)).matches(eq(currentPassword), anyString());
        verify(passwordEncoder, times(0)).encode(newPassword);
        verifyNoMoreInteractions(studentRepository);
        verifyNoMoreInteractions(passwordEncoder);
    }

    @Test
    public void updatePassword_whenPasswordNotCorrect() {
        Student given = new Student("FirstName", "LastName", "112233",
                true, "student@test.pl", "Qwerty.1");
        given.setStudentId(2);
        String currentPassword = "Qwerty.0";
        String newPassword = "Qwerty.2";

        when(studentRepository.findById(given.getStudentId())).thenReturn(Optional.of(given));
        when(passwordEncoder.matches(currentPassword, given.getPassword())).thenReturn(false);

        assertThrows(PasswordNotCorrectException.class, () ->
                studentService.updatePassword(given.getStudentId(), currentPassword, newPassword));

        assertNotEquals(newPassword, given.getPassword());
        verify(studentRepository, times(1)).findById(given.getStudentId());
        verify(studentRepository, times(0)).save(given);
        verify(passwordEncoder, times(1)).matches(eq(currentPassword), anyString());
        verify(passwordEncoder, times(0)).encode(newPassword);
        verifyNoMoreInteractions(studentRepository);
        verifyNoMoreInteractions(passwordEncoder);
    }

    @Test
    public void deleteStudent_whenFound() {
        Student given = new Student();
        given.setStudentId(2);

        when(studentRepository.existsById(given.getStudentId())).thenReturn(true);
        doNothing().when(studentRepository).deleteById(given.getStudentId());

        studentService.delete(given.getStudentId());

        verify(studentRepository, times(1)).existsById(given.getStudentId());
        verify(studentRepository, times(1)).deleteById(given.getStudentId());
        verifyNoMoreInteractions(studentRepository);
    }

    @Test
    public void deleteStudent_whenNotFound() {
        Student given = new Student();
        given.setStudentId(2);

        when(studentRepository.existsById(given.getStudentId())).thenReturn(false);
        assertThrows(StudentNotFoundException.class, () -> studentService.delete(given.getStudentId()));

        verify(studentRepository, times(1)).existsById(given.getStudentId());
        verify(studentRepository, times(0)).deleteById(given.getStudentId());
        verifyNoMoreInteractions(studentRepository);
    }

}
