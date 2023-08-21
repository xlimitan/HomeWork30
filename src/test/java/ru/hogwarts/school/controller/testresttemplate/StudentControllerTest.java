package ru.hogwarts.school.controller.testresttemplate;

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
import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTest {

    @Autowired
    TestRestTemplate template;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    FacultyRepository facultyRepository;

    @BeforeEach
    void init(){
        template.postForEntity("/student", new Student(null, "Roman Romanov", 25), Student.class);
        template.postForEntity("/student", new Student(null, "Marina Ivanova", 24), Student.class);
    }

    @AfterEach
    void clearDb(){
        studentRepository.deleteAll();
    }

    @Test
    void create(){
        ResponseEntity<Student> response = createStudent("Ivan Ivanov", 20);
//        Проверяем, что вернулось то, что мы передали
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Ivan Ivanov");
        assertThat(response.getBody().getAge()).isEqualTo(20);
    }

    @Test
    void getById(){
        ResponseEntity<Student> student = createStudent("Ivan Ivanov", 20);
//        Запрашиваем id и кладем его в переменную
        assertThat(student.getBody()).isNotNull();
        Long id = student.getBody().getId();
        ResponseEntity<Student> response = template.getForEntity("/student/" + id, Student.class);
//        Проверяем, что вернулся код 200
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        Проверяем тело, что оно не null
        assertThat(response.getBody()).isNotNull();
//        Проверяем, что значение тела, равен значению которого мы запросили
        assertThat(response.getBody().getId()).isEqualTo(id);
        assertThat(response.getBody().getName()).isEqualTo("Ivan Ivanov");
        assertThat(response.getBody().getAge()).isEqualTo(20);
    }

    @Test
    void update(){
        ResponseEntity<Student> response = createStudent("Ivan Ivanov", 20);
//      Достали студента из запроса и положим его в переменную
        Student student = response.getBody();
//        Меняем возраст у студента
        assertThat(student).isNotNull();
        student.setAge(21);

        template.put("/student/" + student.getId(), student);

        response = template.getForEntity("/student/" + student.getId(), Student.class);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getAge()).isEqualTo(21);
    }

    @Test
    void deleteFaculty(){
        ResponseEntity<Student> response = createStudent("Ivan Ivanov", 20);
        assertThat(response.getBody()).isNotNull();
        template.delete("/student/" + response.getBody().getId());
        response = template.getForEntity("/student/" + response.getBody().getId(), Student.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void filtered(){
        ResponseEntity<Collection> response = template
                .getForEntity("/student/filtered?age=25", Collection.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(1);
    }

    @Test
    void byFaculty(){
        Faculty faculty = new Faculty(null, "philosophy", "red");
        ResponseEntity<Faculty> facultyResponseEntity = template.postForEntity("/faculty", faculty, Faculty.class);

        Student student = new Student(null, "Ivan Ivanov", 20);
        student.setFaculty(facultyResponseEntity.getBody());
        ResponseEntity<Student> studentResponseEntity = template.postForEntity("/student", student, Student.class);

        Long facultyId = facultyResponseEntity.getBody().getId();

        ResponseEntity<Collection> students = template
                .getForEntity("/student/by-faculty?facultyId=" + facultyId, Collection.class);

        assertThat(students.getBody().isEmpty()).isFalse();
        Map<String,String> resultStudent = (LinkedHashMap<String,String>) students.getBody().iterator().next();
        assertThat(resultStudent.get("name")).isEqualTo("Ivan Ivanov");
    }

    private ResponseEntity<Student> createStudent(String name, int age) {
        Student request = new Student();
        request.setName(name);
        request.setAge(age);
        ResponseEntity<Student> response = template.postForEntity("/student", request, Student.class);
//        Проверяем, что вернулся код 200
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        Проверяем тело, что оно не null
        assertThat(response.getBody()).isNotNull();
        return response;
    }
}
