package ru.hogwarts.school.controller;

import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    @PostMapping
    public Student create(@RequestBody Student student){
        return service.create(student);
    }

    @PutMapping("/{id}")
    public Student update(@PathVariable("id") Long id,@RequestBody  Student student){
        return service.update(id, student);
    }

    @DeleteMapping("/{id}")
    public Student delete(@PathVariable Long id) {
        return service.remove(id);
    }
    @GetMapping("/{id}")
    public Student getById(@PathVariable Long id){
        return service.getById(id);
    }

    @GetMapping
    public Collection<Student> getAll(){
        return service.getAll();
    }
    @GetMapping("/filtered")
    public Collection<Student> getAllByAge(@RequestParam("age") int age) {
        return service.getAllByAge(age);
    }

    @GetMapping("/age-between")
    public Collection<Student> getAllByAgeBetween(@RequestParam("minAge)") int minAge,
                                                  @RequestParam("maxAge") int maxAge){
        return service.getAllByAge(minAge, maxAge);
    }
    @GetMapping("/by-faculty")
    public Collection<Student> getByFaculty(Long facultyId){
        return service.getByFaculty(facultyId);
    }
}
