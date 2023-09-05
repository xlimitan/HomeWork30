package ru.hogwarts.school.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.AvatarService;
import ru.hogwarts.school.service.StudentService;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;
    private final AvatarService avatarService;

    public StudentController(StudentService studentService, AvatarService avatarService) {
        this.studentService = studentService;
        this.avatarService = avatarService;
    }

    @PostMapping
    public Student create(@RequestBody Student student) {
        return studentService.create(student);
    }

    @PutMapping("/{id}")
    public Student update(@PathVariable("id") Long id, @RequestBody Student student) {
        return studentService.update(id, student);
    }

    @DeleteMapping("/{id}")
    public Student delete(@PathVariable Long id) {
        return studentService.remove(id);
    }

    @GetMapping("/{id}")
    public Student getById(@PathVariable Long id) {
        return studentService.getById(id);
    }

    @GetMapping
    public Collection<Student> getAll() {
        return studentService.getAll();
    }

    @GetMapping("/filtered")
    public Collection<Student> getAllByAge(@RequestParam("age") int age) {
        return studentService.getAllByAge(age);
    }

    @GetMapping("/age-between")
    public Collection<Student> getAllByAgeBetween(@RequestParam int minAge,
                                                  @RequestParam int maxAge) {
        return studentService.getAllByAge(minAge, maxAge);
    }

    @GetMapping("/by-faculty")
    public Collection<Student> getByFaculty(Long facultyId) {
        return studentService.getByFaculty(facultyId);
    }

    @PostMapping(value = "/{studentId}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> save(@PathVariable Long studentId, @RequestBody MultipartFile multipartFile) {
        try {
            return ResponseEntity.ok(avatarService.save(studentId, multipartFile));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
    @GetMapping("/count")
    public Long count(){
        return studentService.count();
    }

    @GetMapping("/average-age")
    public Double averageAge(){
        return studentService.averageAge();
    }

    @GetMapping("/last-five")
    public List<Student> getLastFive(){
        return studentService.getLastStudent(5);
    }

    @GetMapping("/names-by-first-symbol")
    public List<String> getBySymbol(@RequestParam char symbol){
        return studentService.getNamesStartedBy(symbol);
    }
    @GetMapping("/stream/average-age")
    public double getAverageAge() {
        return studentService.getAverageAge();
    }
}
