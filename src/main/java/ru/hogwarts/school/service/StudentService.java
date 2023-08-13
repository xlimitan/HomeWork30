package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;


@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;

    public StudentService(StudentRepository studentRepository,
                          FacultyRepository facultyRepository) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
    }

    public Student create(Student student) {
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
        return studentRepository.findById(id)
                .orElseThrow(StudentNotFoundException::new);
    }

    public Collection<Student> getAll() {
        return studentRepository.findAll();
    }

    public Student remove(Long id) {
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
        return studentRepository.findAllByAge(age);

//        Было раньше
//
//        return storage.values().stream()
//                .filter(f -> f.getAge() == age)
//                .collect(Collectors.toList());
    }

    public Collection<Student> getAllByAge(int minAge, int maxAge) {
        return studentRepository.findAllByAgeBetween(minAge, maxAge);
    }

    public Collection<Student> getByFaculty(Long facultyId){
        return facultyRepository.findById(facultyId)
                .map(Faculty::getStudents)
                .orElseThrow(FacultyNotFoundException::new);
    }
}
