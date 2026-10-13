package ru.hogwarts.school;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.StudentRepository;

import java.util.Collection;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTestWithTestRestTemplate { // TestRestTemplate использует оригинал БД с реальными HTTP-запросами + Tomcat на случайном порту

    @LocalServerPort //добавится сгенерированный RANDOM_PORT порт
    private int port;

//    @Autowired конфликт версий
//    private TestRestTemplate restTemplate;

    @Autowired
    private StudentRepository studentRepository; // нужен только для setUP

    @BeforeEach
    void setUp() {
        studentRepository.deleteAll();  //обнуляет БД, чтобы можно было проверить количество в каждом тесте
    }

    private final TestRestTemplate restTemplate = new TestRestTemplate();

    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    @Test
    @DisplayName("создание студента")
    void createStudent() {
        Student student = new Student("GarikTest", 12);
        ResponseEntity<Student> response = restTemplate.postForEntity(url("/student"), student, Student.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody().getId()).isNotNull();
        Assertions.assertThat(response.getBody().getName()).isEqualTo("GarikTest");
        Assertions.assertThat(response.getBody().getAge()).isEqualTo(12);
    }

    @Test
    @DisplayName("поиск студента по id")
    void getStudent() {
        Student student = new Student("RonDonDonTest", 12);
        Student created = restTemplate.postForEntity(url("/student"), student, Student.class).getBody();
        ResponseEntity<Student> response = restTemplate.getForEntity(url("/student/" + created.getId()), Student.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody().getName()).isEqualTo("RonDonDonTest");
    }

    @Test
    @DisplayName("получить всех студентов")
    void getAllStudents() {
        restTemplate.postForEntity(url("/student"), new Student("SJTest", 22), Student.class);
        restTemplate.postForEntity(url("/student"), new Student("RiderTest", 20), Student.class);
        ResponseEntity<Collection<Student>> response = restTemplate.exchange(url("/student"),
                HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotEmpty();
        Assertions.assertThat(response.getBody()).hasSize(2);
    }

    @Test
    @DisplayName("фильтр по возрасту")
    void getStudentsByAgeBetween() {
        restTemplate.postForEntity(url("/student"), new Student("s1Test", 15), Student.class);
        restTemplate.postForEntity(url("/student"), new Student("s2Test", 20), Student.class);
        restTemplate.postForEntity(url("/student"), new Student("s3Test", 16), Student.class);
        ResponseEntity<Collection<Student>> response = restTemplate.exchange(url("/student/filterByAgeBetween?min=10&max=18"),
                HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotEmpty();
        Assertions.assertThat(response.getBody()).hasSize(2);

    }

    @Test
    @DisplayName("обновление студента")
    void updateStudent() {
        Student student = new Student("noNameTest", 13);
        Student created = restTemplate.postForEntity(url("/student"), student, Student.class).getBody();
        created.setAge(14);

        HttpEntity<Student> request = new HttpEntity<>(created);
        ResponseEntity<Student> response = restTemplate.exchange(url("/student"),
                HttpMethod.PUT, request, Student.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody().getAge()).isEqualTo(14);
    }

    @Test
    @DisplayName("удаление студента")
    void deleteStudent() {
        Student student = new Student("noNameTest", 20);
        Student created = restTemplate.postForEntity(url("/student"), student, Student.class).getBody();
        ResponseEntity<Student> response = restTemplate.exchange(url("/student/" + created.getId()),
                HttpMethod.DELETE, null, Student.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();
    }
}
