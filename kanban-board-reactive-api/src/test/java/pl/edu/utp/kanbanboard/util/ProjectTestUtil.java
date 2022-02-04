package pl.edu.utp.kanbanboard.util;

import pl.edu.utp.kanbanboard.model.Project;

import java.time.LocalDateTime;

public class ProjectTestUtil {

    public static Project getEmptyProject() {
        return new Project();
    }

    public static Project getProject1() {
        LocalDateTime time;
        return new Project("1",
                "Web API",
                "projekt internetowego API"
        );
    }

    public static Project getProject2() {
        LocalDateTime time;
        return new Project("2",
                "library DB",
                "projekt bazy danych biblioteki"
        );
    }

    public static Project getProject3() {
        LocalDateTime time;
        return new Project("3",
                "PSOv2",
                "projekt algorytmu optymalizacji rojem cząstek"
        );
    }
}
