package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class FacultyService {

    private static final Logger logger = LoggerFactory.getLogger(FacultyService.class);

    private final FacultyRepository facultyRepository;
    private final StudentRepository studentRepository;
    private final StudentService studentService;

    public FacultyService(FacultyRepository facultyRepository,
                          StudentRepository studentRepository,
                          StudentService studentService) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
        this.studentService = studentService;
    }

    public Faculty createFaculty(Faculty faculty) {
        logger.info("Was invoked method for create faculty: name={}, color={}",
                faculty.getName(), faculty.getColor());
        logger.debug("createFaculty payload: {}", faculty);
        return facultyRepository.save(faculty);
    }

    public Faculty readFaculty(Long id) {
        logger.info("Was invoked method for read faculty by id={}", id);
        return facultyRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("There is no faculty with id={}", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Faculty not found: " + id);
                });
    }

    public Faculty updateFaculty(Faculty faculty) {
        Long facultyId = faculty.getId();
        logger.info("Was invoked method for update faculty id={}", facultyId);
        logger.debug("updateFaculty payload: {}", faculty);

        if (!facultyRepository.existsById(facultyId)) {
            logger.error("There is no faculty with id={} for update", facultyId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Faculty not found: " + facultyId);
        }
        return facultyRepository.save(faculty);
    }

    public void deleteFaculty(Long id) {
        logger.warn("Was invoked method for delete faculty id={}", id);
        if (!facultyRepository.existsById(id)) {
            logger.error("There is no faculty with id={} for delete", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Faculty not found: " + id);
        }
        facultyRepository.deleteById(id);
    }

    public Collection<Faculty> findFacultiesByColor(String color) {
        logger.debug("Was invoked method for find faculties by color={}", color);
        return facultyRepository.findAllByColorIgnoreCase(color);
    }

    public List<Student> getStudentsByFaculty(Long id) {
        logger.debug("Was invoked method for get students by faculty id={}", id);
        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("There is no faculty with id={} to read students", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Faculty not found: " + id);
                });
        return faculty.getStudents();
    }

    public Optional<Faculty> findFacultyByColorOrName(String colorOrName) {
        logger.debug("Was invoked method for find faculty by color or name: value={}", colorOrName);
        return facultyRepository.findFirstByColorIgnoreCaseOrNameIgnoreCase(colorOrName, colorOrName);
    }
}
