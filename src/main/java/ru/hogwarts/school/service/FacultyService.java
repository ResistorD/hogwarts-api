package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;
    private final StudentRepository studentRepository;
    private final StudentService studentService;

    @Autowired
    public FacultyService(FacultyRepository facultyRepository,
                          StudentRepository studentRepository,
                          @Lazy StudentService studentService) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
        this.studentService = studentService;
    }

    public Faculty createFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public Faculty readFaculty(Long id) {
        return facultyRepository.findById(id).orElse(null);
    }

    public Faculty updateFaculty(Faculty faculty) {
        Long facultyId = faculty.getId();
        if (!facultyRepository.existsById(facultyId)) {
            throw new IllegalArgumentException("Faculty not found with id: " + facultyId);
        }
        return facultyRepository.save(faculty);
    }

    public void deleteFaculty(Long id) {
        facultyRepository.deleteById(id);
    }

    public Collection<Faculty> findFacultiesByColor(String color) {
        return facultyRepository.findAllByColorIgnoreCase(color);
    }

    public List<Student> getStudentsByFaculty(Long id) {
        Faculty faculty = facultyRepository.findById(id).orElse(null);
        if (faculty == null) return List.of();
        return faculty.getStudents();
    }

    public Optional<Faculty> findFacultyByColorOrName(String colorOrName) {
        return facultyRepository.findFirstByColorIgnoreCaseOrNameIgnoreCase(colorOrName, colorOrName);
    }
}
