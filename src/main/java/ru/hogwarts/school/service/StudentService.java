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

@Service
public class StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;

    public StudentService(StudentRepository studentRepository, FacultyRepository facultyRepository) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
    }

    public Student createStudent(Student student) {
        logger.info("Was invoked method for create student: name={}, age={}",
                student.getName(), student.getAge());
        logger.debug("createStudent payload: {}", student);

        Long facultyId = student.getFaculty().getId();
        Faculty faculty = facultyRepository.findById(facultyId)
                .orElseThrow(() -> {
                    logger.error("There is no faculty with id={}", facultyId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Faculty not found: " + facultyId);
                });
        student.setFaculty(faculty);
        return studentRepository.save(student);
    }

    public Student getStudent(Long id) {
        logger.info("Was invoked method for get student by id={}", id);
        return studentRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("There is no student with id={}", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found: " + id);
                });
    }

    public Student updateStudent(Student student) {
        logger.info("Was invoked method for update student id={}", student.getId());
        logger.debug("updateStudent payload: {}", student);

        Long studentId = student.getId();
        if (!studentRepository.existsById(studentId)) {
            logger.error("There is no student with id={} for update", studentId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found: " + studentId);
        }

        Long facultyId = student.getFaculty().getId();
        Faculty faculty = facultyRepository.findById(facultyId)
                .orElseThrow(() -> {
                    logger.error("There is no faculty with id={} for update", facultyId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Faculty not found: " + facultyId);
                });

        student.setFaculty(faculty);
        return studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        logger.warn("Was invoked method for delete student id={}", id);
        if (!studentRepository.existsById(id)) {
            logger.error("There is no student with id={} for delete", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found: " + id);
        }
        studentRepository.deleteById(id);
    }

    public Collection<Student> getAllStudents() {
        logger.debug("Was invoked method for get all students");
        return studentRepository.findAll();
    }

    public Collection<Student> findStudentsByAge(int age) {
        logger.debug("Was invoked method for find students by age={}", age);
        return studentRepository.findAllByAge(age);
    }

    public List<Student> findByAgeBetween(int min, int max) {
        logger.debug("Was invoked method for find students by age between min={}, max={}", min, max);
        return studentRepository.findAllByAgeBetween(min, max);
    }

    public Faculty getFacultyByStudent(Long studentId) {
        logger.debug("Was invoked method for get faculty by studentId={}", studentId);
        return studentRepository.findById(studentId)
                .map(Student::getFaculty)
                .orElseThrow(() -> {
                    logger.error("There is no student with id={} to read faculty", studentId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found: " + studentId);
                });
    }
}
