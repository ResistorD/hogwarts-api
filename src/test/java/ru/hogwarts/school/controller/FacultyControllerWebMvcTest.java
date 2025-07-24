package ru.hogwarts.school.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SuppressWarnings("removal")
@WebMvcTest(FacultyController.class)
class FacultyControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean

    private FacultyService facultyService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createFaculty_shouldReturnCreatedFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Gryffindor");
        faculty.setColor("red");

        Mockito.when(facultyService.createFaculty(Mockito.any(Faculty.class)))
                .thenReturn(faculty);

        mockMvc.perform(post("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(faculty)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Gryffindor"))
                .andExpect(jsonPath("$.color").value("red"));
    }

    @Test
    void getFacultyById_shouldReturnFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Ravenclaw");
        faculty.setColor("blue");

        Mockito.when(facultyService.readFaculty(1L)).thenReturn(faculty);

        mockMvc.perform(get("/faculty/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Ravenclaw"))
                .andExpect(jsonPath("$.color").value("blue"));
    }

    @Test
    void updateFaculty_shouldReturnUpdatedFaculty() throws Exception {
        Faculty updatedFaculty = new Faculty();
        updatedFaculty.setId(1L);
        updatedFaculty.setName("Updated Ravenclaw");
        updatedFaculty.setColor("silver-blue");

        Mockito.when(facultyService.updateFaculty(Mockito.any(Faculty.class)))
                .thenReturn(updatedFaculty);

        mockMvc.perform(put("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedFaculty)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated Ravenclaw"))
                .andExpect(jsonPath("$.color").value("silver-blue"));
    }

    @Test
    void deleteFaculty_shouldReturnOkStatus() throws Exception {
        Long id = 1L;

        Mockito.doNothing().when(facultyService).deleteFaculty(id);

        mockMvc.perform(delete("/faculty/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    void getFacultiesByColor_shouldReturnMatchingFaculties() throws Exception {
        Faculty f1 = new Faculty(1L, "Alpha", "silver");
        Faculty f2 = new Faculty(2L, "Beta", "silver");

        Mockito.when(facultyService.findFacultiesByColor("silver"))
                .thenReturn(List.of(f1, f2));

        mockMvc.perform(get("/faculty/color")
                        .param("color", "silver"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Alpha"))
                .andExpect(jsonPath("$[1].name").value("Beta"));
    }

    @Test
    void getStudentsByFaculty_shouldReturnAttachedStudents() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Gryffindor");
        faculty.setColor("red");

        Student s1 = new Student(1L, "Harry Potter", 14, faculty);
        Student s2 = new Student(2L, "Hermione Granger", 14, faculty);

        Mockito.when(facultyService.getStudentsByFaculty(1L))
                .thenReturn(List.of(s1, s2));

        mockMvc.perform(get("/faculty/1/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Harry Potter"))
                .andExpect(jsonPath("$[1].name").value("Hermione Granger"));
    }

    @Test
    void findByColorOrName_shouldReturnMatchingFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Slytherin");
        faculty.setColor("green");

        // Поиск по имени
        Mockito.when(facultyService.findFacultyByColorOrName("Slytherin"))
                .thenReturn(Optional.of(faculty));

        mockMvc.perform(get("/faculty/search")
                        .param("value", "Slytherin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Slytherin"))
                .andExpect(jsonPath("$.color").value("green"));

        // Поиск по цвету
        Mockito.when(facultyService.findFacultyByColorOrName("green"))
                .thenReturn(Optional.of(faculty));

        mockMvc.perform(get("/faculty/search")
                        .param("value", "green"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Slytherin"))
                .andExpect(jsonPath("$.color").value("green"));
    }

}
