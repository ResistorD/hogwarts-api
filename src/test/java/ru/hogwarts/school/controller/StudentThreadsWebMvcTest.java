package ru.hogwarts.school.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school.service.StudentService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
class StudentThreadsWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    @SuppressWarnings("unused")
    private StudentService studentService;

    @Test
    void printParallel_shouldReturnOkAndDelegateToService() throws Exception {
        mockMvc.perform(get("/student/print-parallel"))
                .andExpect(status().isOk());

        Mockito.verify(studentService, Mockito.times(1)).printStudentsParallel();
    }

    @Test
    void printSynchronized_shouldReturnOkAndDelegateToService() throws Exception {
        mockMvc.perform(get("/student/print-synchronized"))
                .andExpect(status().isOk());

        Mockito.verify(studentService, Mockito.times(1)).printStudentsSynchronized();
    }
}
