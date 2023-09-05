package ru.hogwarts.school.service;

import org.apache.commons.lang3.CharUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;
    private final AvatarRepository avatarRepository;

    public StudentService(StudentRepository studentRepository,
                          FacultyRepository facultyRepository,
                          AvatarRepository avatarRepository) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
        this.avatarRepository = avatarRepository;
    }

    public Student create(Student student) {
        logger.info("Run method create");
        return studentRepository.save(student);
//        Было раньше
//
//        Long id = student.getId();
//        if (id != null && storage.containsKey(id)) {
//            throw new StudentAlreadyExistsException();
//        }
//        Long nextInt = COUNTER++;
//        student.setId(nextInt);
//        storage.put(nextInt, student);
//        return student;
    }

    public Student update(Long id, Student student) {
        logger.info("Run method update");
        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(StudentNotFoundException::new);
        existingStudent.setAge(student.getAge());
        existingStudent.setName(student.getName());
        return studentRepository.save(existingStudent);

//        Было раньше
//        if (!storage.containsKey(id)) {
//            throw new StudentNotFoundException();
//        }
//        student.setId(id);
//        storage.put(id, student);
//        return student;
    }

    public Student getById(Long id) {
        logger.info("Run method getById");
        return studentRepository.findById(id)
                .orElseThrow(StudentNotFoundException::new);
    }

    public Collection<Student> getAll() {
        logger.info("Run method getAll");
        return studentRepository.findAll();
    }

    @Transactional
    public Student remove(Long id) {
        logger.info("Run method remove");
        avatarRepository.deleteByStudent_id(id);
        Student student = studentRepository.findById(id)
                .orElseThrow(StudentNotFoundException::new);
        studentRepository.delete(student);
        return student;

//        Было раньше
//
//        if (!storage.containsKey(id)) {
//            throw new StudentNotFoundException();
//        }
//        return storage.remove(id);
    }

    public Collection<Student> getAllByAge(int age) {
        logger.info("Run method getAllByAge");
        return studentRepository.findAllByAge(age);

//        Было раньше
//
//        return storage.values().stream()
//                .filter(f -> f.getAge() == age)
//                .collect(Collectors.toList());
    }

    public Collection<Student> getAllByAge(int minAge, int maxAge) {
        logger.info("Run method getAllByAge");
        return studentRepository.findAllByAgeBetween(minAge, maxAge);
    }

    public Collection<Student> getByFaculty(Long facultyId) {
        logger.info("Run method getByFaculty");
        return facultyRepository.findById(facultyId)
                .map(Faculty::getStudents)
                .orElseThrow(FacultyNotFoundException::new);
    }

    public Long count() {
        logger.info("Run method count");
        return studentRepository.countAll();
    }

    public Double averageAge() {
        logger.info("Run method averageAge");
        return studentRepository.averageAge();
    }

    public List<Student> getLastStudent(int num) {
        logger.info("Run method getLastStudent");
        return studentRepository.findLastStudent(num);
    }
    public List<String> getNamesStartedBy(char firstSymbol){
        return studentRepository.findAll().stream()
                .map(Student::getName)
                .filter(n ->Character.toLowerCase(n.charAt(0))==Character.toLowerCase(firstSymbol))
                .collect(Collectors.toList());
    }
    public double getAverageAge(){
        return studentRepository.findAll()
                .stream()
                .mapToInt(Student::getAge)
                .average()
                .orElseThrow(StudentNotFoundException::new);
    }
}
