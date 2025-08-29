package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/faculty")
public class FacultyController {

    private final FacultyService facultyService;
    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Faculty createFaculty(@RequestBody Faculty faculty) {
        return facultyService.createFaculty(faculty);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Faculty readFaculty(@PathVariable Long id) {
        return facultyService.readFaculty(id);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Faculty updateFaculty(@RequestBody Faculty faculty) {
        return facultyService.updateFaculty(faculty);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFaculty(@PathVariable Long id) {
        facultyService.deleteFaculty(id);
    }

    @GetMapping("/color")
    @ResponseStatus(HttpStatus.OK)
    public Collection<Faculty> getFacultiesByColor(@RequestParam String color) {
        return facultyService.findFacultiesByColor(color);
    }

    @GetMapping("/{id}/students")
    @ResponseStatus(HttpStatus.OK)
    public List<Student> getStudentsByFaculty(@PathVariable Long id) {
        return facultyService.getStudentsByFaculty(id);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public Faculty findByColorOrName(@RequestParam String value) {
        return facultyService.findFacultyByColorOrName(value)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Faculty not found by: " + value));
    }

    @GetMapping("/longest-name")
    @ResponseStatus(HttpStatus.OK)
    public String getLongestFacultyName() {
        return facultyService.getLongestFacultyName();
    }
}
