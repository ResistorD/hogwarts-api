package ru.hogwarts.school.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FacultyControllerRestTemplateTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void createFaculty_shouldReturnCreatedFaculty() {
        Faculty testFaculty = new Faculty();
        testFaculty.setName("Gryffindor");
        testFaculty.setColor("red");

        ResponseEntity<Faculty> response = restTemplate.postForEntity("/faculty", testFaculty, Faculty.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Faculty created = response.getBody();
        assertNotNull(created);
        assertNotNull(created.getId());
        assertEquals("Gryffindor", created.getName());
        assertEquals("red", created.getColor());
    }

    @Test
    void getFacultyById_shouldReturnCorrectFaculty() {
        // Создаём факультет
        Faculty faculty = new Faculty();
        faculty.setName("Ravenclaw");
        faculty.setColor("blue");

        ResponseEntity<Faculty> postResponse =
                restTemplate.postForEntity("/faculty", faculty, Faculty.class);

        assertEquals(HttpStatus.OK, postResponse.getStatusCode());
        Faculty created = postResponse.getBody();
        assertNotNull(created);
        Long id = created.getId();
        assertNotNull(id);

        // Получаем по ID
        ResponseEntity<Faculty> getResponse =
                restTemplate.getForEntity("/faculty/" + id, Faculty.class);

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        Faculty fetched = getResponse.getBody();
        assertNotNull(fetched);
        assertEquals("Ravenclaw", fetched.getName());
        assertEquals("blue", fetched.getColor());
    }

    @Test
    void updateFaculty_shouldReturnUpdatedFaculty() {
        // Создаём факультет
        Faculty faculty = new Faculty();
        faculty.setName("Hufflepuff");
        faculty.setColor("yellow");

        ResponseEntity<Faculty> postResponse =
                restTemplate.postForEntity("/faculty", faculty, Faculty.class);

        assertEquals(HttpStatus.OK, postResponse.getStatusCode());
        Faculty created = postResponse.getBody();
        assertNotNull(created);
        Long id = created.getId();

        // Обновляем название и цвет
        created.setName("Updated Hufflepuff");
        created.setColor("gold");

        HttpEntity<Faculty> requestEntity = new HttpEntity<>(created);
        ResponseEntity<Faculty> putResponse =
                restTemplate.exchange("/faculty", HttpMethod.PUT, requestEntity, Faculty.class);

        assertEquals(HttpStatus.OK, putResponse.getStatusCode());
        Faculty updated = putResponse.getBody();
        assertNotNull(updated);
        assertEquals(id, updated.getId());
        assertEquals("Updated Hufflepuff", updated.getName());
        assertEquals("gold", updated.getColor());
    }

    @Test
    void deleteFaculty_shouldRemoveFaculty() {
        // Создаём факультет
        Faculty faculty = new Faculty();
        faculty.setName("Slytherin");
        faculty.setColor("green");

        ResponseEntity<Faculty> postResponse =
                restTemplate.postForEntity("/faculty", faculty, Faculty.class);

        assertEquals(HttpStatus.OK, postResponse.getStatusCode());
        Faculty created = postResponse.getBody();
        assertNotNull(created);
        Long id = created.getId();

        // Удаляем
        restTemplate.delete("/faculty/" + id);

        // Проверяем, что факультет удалён
        ResponseEntity<String> getResponse =
                restTemplate.getForEntity("/faculty/" + id, String.class);

        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }

    @Test
    void getFacultiesByColor_shouldReturnMatchingFaculties() {
        // Добавляем два факультета с нужным цветом и один с другим
        Faculty f1 = new Faculty();
        f1.setName("Alpha");
        f1.setColor("silver");

        Faculty f2 = new Faculty();
        f2.setName("Beta");
        f2.setColor("silver");

        Faculty f3 = new Faculty();
        f3.setName("Gamma");
        f3.setColor("bronze");

        restTemplate.postForEntity("/faculty", f1, Faculty.class);
        restTemplate.postForEntity("/faculty", f2, Faculty.class);
        restTemplate.postForEntity("/faculty", f3, Faculty.class);

        // Выполняем запрос по цвету "silver"
        ResponseEntity<Faculty[]> response =
                restTemplate.getForEntity("/faculty/color?color=silver", Faculty[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Faculty[] result = response.getBody();
        assertNotNull(result);
        assertTrue(result.length >= 2);

        for (Faculty faculty : result) {
            assertEquals("silver", faculty.getColor());
        }
    }

    @Test
    void getStudentsByFaculty_shouldReturnAttachedStudents() {
        // Создаём факультет
        Faculty faculty = new Faculty();
        faculty.setName("TestFaculty");
        faculty.setColor("testColor");

        ResponseEntity<Faculty> facultyResponse =
                restTemplate.postForEntity("/faculty", faculty, Faculty.class);
        Faculty createdFaculty = facultyResponse.getBody();
        assertNotNull(createdFaculty);
        Long facultyId = createdFaculty.getId();

        // Создаём студентов и прикрепляем к факультету
        Student s1 = new Student();
        s1.setName("Dean Thomas");
        s1.setAge(14);
        s1.setFaculty(createdFaculty);

        Student s2 = new Student();
        s2.setName("Seamus Finnigan");
        s2.setAge(14);
        s2.setFaculty(createdFaculty);

        restTemplate.postForEntity("/student", s1, Student.class);
        restTemplate.postForEntity("/student", s2, Student.class);

        // Получаем студентов по факультету
        ResponseEntity<Student[]> response =
                restTemplate.getForEntity("/faculty/" + facultyId + "/students", Student[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Student[] result = response.getBody();
        assertNotNull(result);
        assertTrue(result.length >= 2);
        for (Student s : result) {
            assertEquals(facultyId, s.getFaculty().getId());
        }
    }

    @Test
    void findByColorOrName_shouldReturnMatchingFaculty() {
        // Создаём факультет
        Faculty faculty = new Faculty();
        faculty.setName("Phoenix");
        faculty.setColor("crimson");

        ResponseEntity<Faculty> postResponse =
                restTemplate.postForEntity("/faculty", faculty, Faculty.class);
        assertEquals(HttpStatus.OK, postResponse.getStatusCode());
        Faculty created = postResponse.getBody();
        assertNotNull(created);

        // Поиск по имени
        ResponseEntity<Faculty> byNameResponse =
                restTemplate.getForEntity("/faculty/search?value=Phoenix", Faculty.class);

        assertEquals(HttpStatus.OK, byNameResponse.getStatusCode());
        Faculty foundByName = byNameResponse.getBody();
        assertNotNull(foundByName);
        assertEquals("Phoenix", foundByName.getName());

        // Поиск по цвету
        ResponseEntity<Faculty> byColorResponse =
                restTemplate.getForEntity("/faculty/search?value=crimson", Faculty.class);

        assertEquals(HttpStatus.OK, byColorResponse.getStatusCode());
        Faculty foundByColor = byColorResponse.getBody();
        assertNotNull(foundByColor);
        assertEquals("crimson", foundByColor.getColor());
    }

}
