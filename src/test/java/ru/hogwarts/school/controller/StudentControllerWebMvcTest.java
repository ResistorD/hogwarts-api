package ru.hogwarts.school.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
class StudentControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    @SuppressWarnings("unused") // поле заполняется контейнером, а не вручную
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getStudentById_shouldReturnStudent() throws Exception {
        Student student = new Student(1L, "Harry Potter", 14, null);

        Mockito.when(studentService.getStudent(1L)).thenReturn(student);

        mockMvc.perform(get("/student/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Harry Potter"))
                .andExpect(jsonPath("$.age").value(14));
    }

    @Test
    void createStudent_shouldReturnCreatedStudent() throws Exception {
        Student student = new Student(1L, "Hermione Granger", 13, null);

        Mockito.when(studentService.createStudent(Mockito.any(Student.class))).thenReturn(student);

        mockMvc.perform(post("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Hermione Granger"))
                .andExpect(jsonPath("$.age").value(13));
    }

    @Test
    void updateStudent_shouldReturnUpdatedStudent() throws Exception {
        Student student = new Student(1L, "Ronald Weasley", 15, null);

        Mockito.when(studentService.updateStudent(Mockito.any(Student.class))).thenReturn(student);

        mockMvc.perform(put("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Ronald Weasley"))
                .andExpect(jsonPath("$.age").value(15));
    }

    @Test
    void deleteStudent_shouldReturnNoContentStatus() throws Exception {
        Long id = 1L;

        Mockito.doNothing().when(studentService).deleteStudent(id);

        mockMvc.perform(delete("/student/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void getStudentsByAge_shouldReturnFilteredStudents() throws Exception {
        Student s1 = new Student(1L, "Luna Lovegood", 13, null);
        Student s2 = new Student(2L, "Colin Creevey", 13, null);

        Mockito.when(studentService.findStudentsByAge(13))
                .thenReturn(List.of(s1, s2));

        mockMvc.perform(get("/student/filter")
                        .param("age", "13"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Luna Lovegood"))
                .andExpect(jsonPath("$[1].name").value("Colin Creevey"));
    }

    @Test
    void getStudentsByAgeRange_shouldReturnStudentsInRange() throws Exception {
        Student s1 = new Student(1L, "Fred Weasley", 12, null);
        Student s2 = new Student(2L, "George Weasley", 14, null);

        Mockito.when(studentService.findByAgeBetween(12, 14))
                .thenReturn(List.of(s1, s2));

        mockMvc.perform(get("/student/age-between")
                        .param("min", "12")
                        .param("max", "14"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Fred Weasley"))
                .andExpect(jsonPath("$[1].name").value("George Weasley"));
    }
}
