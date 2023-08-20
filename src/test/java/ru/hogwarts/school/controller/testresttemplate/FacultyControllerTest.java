package ru.hogwarts.school.controller.testresttemplate;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.Application;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyControllerTest {
    public static final Faculty HISTORY = new Faculty(null, "history", "red");
    public static final Faculty PHILOSOPHY = new Faculty(null, "philosophy", "black");
    @Autowired
    TestRestTemplate template;

    @Autowired
    FacultyRepository facultyRepository;

    @Autowired
    StudentRepository studentRepository;

    @BeforeEach
    void init(){
        template.postForEntity("/faculty", HISTORY, Faculty.class);
        template.postForEntity("/faculty", PHILOSOPHY, Faculty.class);
    }

    @AfterEach
    void clearDb(){
        studentRepository.deleteAll();
        facultyRepository.deleteAll();
    }


    @Test
    void create(){
        ResponseEntity<Faculty> response = createFaculty("math", "blue");
//        Проверяем, что вернулось то, что мы передали
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("math");
        assertThat(response.getBody().getColor()).isEqualTo("blue");
    }

    @Test
    void getById(){
        ResponseEntity<Faculty> faculty = createFaculty("math", "blue");
//        Запрашиваем id и кладем его в переменную
        assertThat(faculty.getBody()).isNotNull();
        Long id = faculty.getBody().getId();
        ResponseEntity<Faculty> response = template.getForEntity("/faculty/" + id, Faculty.class);
//        Проверяем, что вернулся код 200
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        Проверяем тело, что оно не null
        assertThat(response.getBody()).isNotNull();
//        Проверяем, что значение тела, равен значению которого мы запросили
        assertThat(response.getBody().getId()).isEqualTo(id);
        assertThat(response.getBody().getName()).isEqualTo("math");
        assertThat(response.getBody().getColor()).isEqualTo("blue");
    }

    @Test
    void getAll(){
        ResponseEntity<Collection> response = template.getForEntity("/faculty", Collection.class);
//        Проверяем, что вернулся код 200
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        Проверяем тело, что оно не null
        assertThat(response.getBody()).isNotNull();
//        Проверяем, что в теле есть элементы
        Collection<Faculty> body = response.getBody();
        assertThat(body.isEmpty()).isFalse();
//        Проверяем размер
        assertThat(body.size()).isEqualTo(2);
    }
    @Test
    void update(){
        ResponseEntity<Faculty> response = createFaculty("math", "blue");
//      Достали факультет из запроса и положили его в переменную
        Faculty faculty = response.getBody();
//        Меняем цвет у факультета
        assertThat(faculty).isNotNull();
        assertThat(faculty.getColor()).isNotNull();
        faculty.setColor("green");

        template.put("/faculty/" + faculty.getId(), faculty);

        response = template.getForEntity("/faculty/" + faculty.getId(), Faculty.class);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getColor()).isNotNull();
        assertThat(response.getBody().getColor()).isEqualTo("green");
    }

    @Test
    void deleteFaculty(){
        ResponseEntity<Faculty> response = createFaculty("math", "blue");
        assertThat(response.getBody()).isNotNull();
        template.delete("/faculty/" + response.getBody().getId());
        response = template.getForEntity("/faculty/" + response.getBody().getId(), Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void filtered(){
        ResponseEntity<Collection> response = template
                .getForEntity("/faculty/color?color=red", Collection.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(1);
    }

    @Test
    void byStudent(){
        ResponseEntity<Faculty> response = createFaculty("math", "blue");
        Faculty expectedFaculty = response.getBody();
        Student student = new Student();
        student.setFaculty(expectedFaculty);
        ResponseEntity<Student> studentResponseEntity = template.postForEntity("/student", student, Student.class);
        assertThat(studentResponseEntity.getBody()).isNotNull();
        Long studentId = studentResponseEntity.getBody().getId();
        response = template.getForEntity("/faculty/by-student?studentId="+ studentId,Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo(expectedFaculty);
    }

    private ResponseEntity<Faculty> createFaculty(String name, String color) {
        Faculty request = new Faculty();
        request.setName(name);
        request.setColor(color);
        ResponseEntity<Faculty> response = template.postForEntity("/faculty", request, Faculty.class);
//        Проверяем, что вернулся код 200
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        Проверяем тело, что оно не null
        assertThat(response.getBody()).isNotNull();
        return response;
    }
}
