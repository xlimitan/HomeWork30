package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.List;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    List<Faculty> findAllByColor(String color);

    Faculty findFacultyByNameIgnoreCaseOrColorIgnoreCase(String name, String color);

//    List<Faculty> findAllByStudents(List<Student> student);

}
