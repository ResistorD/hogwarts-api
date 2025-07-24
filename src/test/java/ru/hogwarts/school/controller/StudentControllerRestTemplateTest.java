package ru.hogwarts.school.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.hogwarts.school.model.Student;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerRestTemplateTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void getAllStudents_shouldReturnOk() {
        ResponseEntity<String> response = restTemplate.getForEntity("/student", String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void createStudent_shouldReturnCreatedStudent() {

        Student testStudent = new Student();
        testStudent.setName("Harry Potter");
        testStudent.setAge(14);

        ResponseEntity<Student> response = restTemplate.postForEntity("/student", testStudent, Student.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Student created = response.getBody();
        assertNotNull(created);
        assertNotNull(created.getId());
        assertEquals("Harry Potter", created.getName());
        assertEquals(14, created.getAge());
    }

    @Test
    void getStudentById_shouldReturnCorrectStudent() {

        // Создаём студента
        Student student = new Student();
        student.setName("Hermione Granger");
        student.setAge(13);

        ResponseEntity<Student> postResponse =
                restTemplate.postForEntity("/student", student, Student.class);

        assertEquals(HttpStatus.OK, postResponse.getStatusCode());
        Student createdStudent = postResponse.getBody();
        assertNotNull(createdStudent);
        Long id = createdStudent.getId();
        assertNotNull(id);

        // Получаем по ID
        ResponseEntity<Student> getResponse =
                restTemplate.getForEntity("/student/" + id, Student.class);

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        Student fetched = getResponse.getBody();
        assertNotNull(fetched);
        assertEquals("Hermione Granger", fetched.getName());
        assertEquals(13, fetched.getAge());
    }

    @Test
    void updateStudent_shouldReturnUpdatedStudent() {

        // Создаём студента
        Student student = new Student();
        student.setName("Ron Weasley");
        student.setAge(14);

        ResponseEntity<Student> postResponse =
                restTemplate.postForEntity("/student", student, Student.class);

        assertEquals(HttpStatus.OK, postResponse.getStatusCode());
        Student created = postResponse.getBody();
        assertNotNull(created);
        Long id = created.getId();
        assertNotNull(id);

        // Обновляем имя и возраст
        created.setName("Ronald Weasley");
        created.setAge(15);

        HttpEntity<Student> requestEntity = new HttpEntity<>(created);
        ResponseEntity<Student> putResponse =
                restTemplate.exchange("/student", HttpMethod.PUT, requestEntity, Student.class);

        assertEquals(HttpStatus.OK, putResponse.getStatusCode());
        Student updated = putResponse.getBody();
        assertNotNull(updated);
        assertEquals(id, updated.getId());
        assertEquals("Ronald Weasley", updated.getName());
        assertEquals(15, updated.getAge());
    }

    @Test
    void deleteStudent_shouldRemoveStudent() {
        // Создаём студента
        Student student = new Student();
        student.setName("Neville Longbottom");
        student.setAge(13);

        ResponseEntity<Student> postResponse =
                restTemplate.postForEntity("/student", student, Student.class);

        assertEquals(HttpStatus.OK, postResponse.getStatusCode());
        Student created = postResponse.getBody();
        assertNotNull(created);
        Long id = created.getId();

        // Удаляем
        restTemplate.delete("/student/" + id);

        // Проверяем, что он удалён
        ResponseEntity<String> getResponse =
                restTemplate.getForEntity("/student/" + id, String.class);

        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }

    @Test
    void getStudentsByAge_shouldReturnMatchingStudents() {
        // Создаём двух студентов: одного нужного возраста, одного — нет
        Student student1 = new Student();
        student1.setName("Luna Lovegood");
        student1.setAge(13);

        Student student2 = new Student();
        student2.setName("Draco Malfoy");
        student2.setAge(15);

        restTemplate.postForEntity("/student", student1, Student.class);
        restTemplate.postForEntity("/student", student2, Student.class);

        // Запрашиваем по возрасту 13
        ResponseEntity<Student[]> response = restTemplate.getForEntity("/student/filter?age=13", Student[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Student[] filtered = response.getBody();
        assertNotNull(filtered);
        assertTrue(filtered.length >= 1);
        assertTrue(
                java.util.Arrays.stream(filtered)
                        .allMatch(s -> s.getAge() == 13)
        );
    }

    @Test
    void getStudentsByAgeRange_shouldReturnStudentsWithinRange() {
        // Создаём студентов разных возрастов
        Student s1 = new Student();
        s1.setName("Fred Weasley");
        s1.setAge(12);

        Student s2 = new Student();
        s2.setName("George Weasley");
        s2.setAge(14);

        Student s3 = new Student();
        s3.setName("Percy Weasley");
        s3.setAge(16);

        restTemplate.postForEntity("/student", s1, Student.class);
        restTemplate.postForEntity("/student", s2, Student.class);
        restTemplate.postForEntity("/student", s3, Student.class);

        // Запрашиваем диапазон [13, 15]
        ResponseEntity<Student[]> response = restTemplate.getForEntity(
                "/student/age-between?min=13&max=15", Student[].class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Student[] result = response.getBody();
        assertNotNull(result);

        assertTrue(
                java.util.Arrays.stream(result)
                        .allMatch(s -> s.getAge() >= 13 && s.getAge() <= 15)
        );
    }

}
