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
public class FacultyService {

    private final FacultyRepository facultyRepository;
    private final StudentRepository studentRepository;

    public FacultyService(FacultyRepository facultyRepository,
                          StudentRepository studentRepository) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
    }

    public Faculty create(Faculty faculty) {
        return facultyRepository.save(faculty);

//        Было раньше
//
//        Long id = faculty.getId();
//        if (id != null && storage.containsKey(id)) {
//            throw new FacultyAlreadyExistsException();
//        }
//        Long nextInt = COUNTER++;
//        faculty.setId(nextInt);
//        storage.put(nextInt, faculty);
//        return faculty;
    }

    public Faculty update(Long id, Faculty faculty) {
        Faculty existingFaculty = facultyRepository.findById(id)
                .orElseThrow(FacultyNotFoundException::new);
        existingFaculty.setColor(faculty.getColor());
        existingFaculty.setName(faculty.getName());
        return facultyRepository.save(existingFaculty);

//        Было раньше
//
//        if (!storage.containsKey(id)) {
//            throw new FacultyNotFoundException();
//        }
//        faculty.setId(id);
//        storage.put(id, faculty);
//        return faculty;
    }

    public Faculty getById(Long id) {
        return facultyRepository.findById(id)
                .orElseThrow(FacultyNotFoundException::new);
    }

    public Collection<Faculty> getAll() {
        return facultyRepository.findAll();
    }

    public Faculty remove(Long id) {
        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(FacultyNotFoundException::new);
        facultyRepository.delete(faculty);
        return faculty;

//        Было раньше
//
//        if (!storage.containsKey(id)) {
//            throw new FacultyNotFoundException();
//        }
//        return storage.remove(id);
    }

    public Collection<Faculty> getAllByColor(String color) {
        return facultyRepository.findAllByColor(color);

//        Было раньше
//
//        return storage.values().stream()
//                .filter(f -> f.getColor().equalsIgnoreCase(color))
//                .collect(Collectors.toList());
    }

    public Faculty getFacultyByNameOrColor(String name, String color) {
        return facultyRepository.findFacultyByNameIgnoreCaseOrColorIgnoreCase(name, color);
    }

    public Faculty getByStudentId(Long studentId){
        return studentRepository.findById(studentId)
                .map(Student::getFaculty)
                .orElseThrow(StudentNotFoundException::new);
    }
}
