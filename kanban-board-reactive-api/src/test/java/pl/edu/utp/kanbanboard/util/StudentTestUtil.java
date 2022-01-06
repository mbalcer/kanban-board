package pl.edu.utp.kanbanboard.util;

import pl.edu.utp.kanbanboard.model.Student;

public class StudentTestUtil {
    public static Student getStudent1() {
        return new Student("00001111-2222-3333-4444-555566667777",
                "Jan",
                "Kowalski",
                "111000",
                true,
                "jankow@wp.pl",
                "111222");
    }

    public static Student getStudent2() {
        return new Student("00000000-2222-3333-4444-555555555555",
                "Andrzej",
                "Nowak",
                "123321",
                true,
                "andnow@wp.pl",
                "123456");
    }

    public static Student getStudent3() {
        return new Student("00000000-1111-2222-8888-555555555555",
                "Michał",
                "Daniel",
                "122000",
                false,
                "micdan@wp.pl",
                "michal123!");
    }
}
