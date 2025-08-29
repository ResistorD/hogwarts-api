package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // ---------- CRUD ----------
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // 201
    public Student createStudent(@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Student getStudent(@PathVariable Long id) {
        return studentService.getStudent(id);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Student updateStudent(@RequestBody Student student) {
        return studentService.updateStudent(student);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // 204
    public void deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
    }

    // ---------- Queries ----------
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/filter")
    @ResponseStatus(HttpStatus.OK)
    public Collection<Student> getStudentsByAge(@RequestParam int age) {
        return studentService.findStudentsByAge(age);
    }

    @GetMapping("/age-between")
    @ResponseStatus(HttpStatus.OK)
    public List<Student> findByAgeBetween(@RequestParam int min, @RequestParam int max) {
        return studentService.findByAgeBetween(min, max);
    }

    @GetMapping("/{id}/faculty")
    @ResponseStatus(HttpStatus.OK)
    public Faculty getFacultyByStudentId(@PathVariable Long id) {
        return studentService.getFacultyByStudent(id);
    }

    // ---------- HW 4.5 ----------
    /** Список имён на «A/А» в верхнем регистре и отсортированный */
    @GetMapping("/names/a")
    @ResponseStatus(HttpStatus.OK)
    public List<String> getNamesStartingWithA() {
        return studentService.getStudentsNamesStartingWithAUpperSorted();
    }

    /** Средний возраст всех студентов */
    @GetMapping("/age/average")
    @ResponseStatus(HttpStatus.OK)
    public double getAverageAge() {
        return studentService.getAverageAge();
    }
}
