package ru.hogwarts.school.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;

    public StudentService(StudentRepository studentRepository, FacultyRepository facultyRepository) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
    }

    // ---------- CRUD ----------
    public Student createStudent(Student student) {
        log.info("Was invoked method for create student: name={}, age={}", student.getName(), student.getAge());
        log.debug("createStudent payload: {}", student);

        Long facultyId = student.getFaculty().getId();
        Faculty faculty = facultyRepository.findById(facultyId)
                .orElseThrow(() -> {
                    log.error("There is no faculty with id={}", facultyId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Faculty not found: " + facultyId);
                });
        student.setFaculty(faculty);
        return studentRepository.save(student);
    }

    public Student getStudent(Long id) {
        log.info("Was invoked method for get student by id={}", id);
        return studentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("There is no student with id={}", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found: " + id);
                });
    }

    public Student updateStudent(Student student) {
        log.info("Was invoked method for update student id={}", student.getId());
        log.debug("updateStudent payload: {}", student);

        Long studentId = student.getId();
        if (!studentRepository.existsById(studentId)) {
            log.error("There is no student with id={} for update", studentId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found: " + studentId);
        }

        Long facultyId = student.getFaculty().getId();
        Faculty faculty = facultyRepository.findById(facultyId)
                .orElseThrow(() -> {
                    log.error("There is no faculty with id={} for update", facultyId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Faculty not found: " + facultyId);
                });

        student.setFaculty(faculty);
        return studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        log.warn("Was invoked method for delete student id={}", id);
        if (!studentRepository.existsById(id)) {
            log.error("There is no student with id={} for delete", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found: " + id);
        }
        studentRepository.deleteById(id);
    }

    // ---------- Queries ----------
    public Collection<Student> getAllStudents() {
        log.debug("Was invoked method for get all students");
        return studentRepository.findAll();
    }

    public Collection<Student> findStudentsByAge(int age) {
        log.debug("Was invoked method for find students by age={}", age);
        return studentRepository.findAllByAge(age);
    }

    public List<Student> findByAgeBetween(int min, int max) {
        log.debug("Was invoked method for find students by age between min={}, max={}", min, max);
        return studentRepository.findAllByAgeBetween(min, max);
    }

    public Faculty getFacultyByStudent(Long studentId) {
        log.debug("Was invoked method for get faculty by studentId={}", studentId);
        return studentRepository.findById(studentId)
                .map(Student::getFaculty)
                .orElseThrow(() -> {
                    log.error("There is no student with id={} to read faculty", studentId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found: " + studentId);
                });
    }

    // ---------- HW 4.5 ----------
    /** Список имён студентов, начинающихся на «A/А», в ВЕРХНЕМ регистре и отсортированный */
    public List<String> getStudentsNamesStartingWithAUpperSorted() {
        log.debug("Was invoked method for get names starting with A/А");
        return studentRepository.findAll().stream()
                .map(Student::getName)
                .filter(name -> name != null && !name.isBlank())
                .map(name -> name.toUpperCase(Locale.ROOT))
                .filter(upper -> upper.startsWith("A") || upper.startsWith("А"))
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.toList());
    }

    /** Средний возраст всех студентов */
    public double getAverageAge() {
        log.debug("Was invoked method for get average age");
        return studentRepository.findAll().stream()
                .mapToInt(Student::getAge)
                .average()
                .orElse(0.0);
    }

    /** Печать 6 имён: 1-2 в главном потоке, 3-4 в одном параллельном, 5-6 в другом. */
    public void printStudentsParallel() {

    }

    /** Та же раскладка, но с использованием синхронизированного метода печати. */
    public void printStudentsSynchronized() {

    }
}
