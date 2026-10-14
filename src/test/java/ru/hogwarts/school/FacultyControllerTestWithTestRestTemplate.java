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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repositories.FacultyRepository;
import ru.hogwarts.school.repositories.StudentRepository;

import java.util.Collection;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyControllerTestWithTestRestTemplate {

    @LocalServerPort
    private int port;

    @Autowired
    private FacultyRepository facultyRepository;

    @BeforeEach
    void setUp() {
        facultyRepository.deleteAll();
    }

    private final TestRestTemplate restTemplate = new TestRestTemplate();

    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    @Test
    @DisplayName("создание факультета")
    void createFaculty() {
        Faculty faculty = new Faculty("GroveStreetTest", "Green");
        ResponseEntity<Faculty> response = restTemplate.postForEntity(url("/faculty"), faculty, Faculty.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody().getId()).isNotNull();
        Assertions.assertThat(response.getBody().getName()).isEqualTo("GroveStreetTest");
        Assertions.assertThat(response.getBody().getColor()).isEqualTo("Green");
    }

    @Test
    @DisplayName("поиск факультета по id")
    void getFaculty() {
        Faculty faculty = new Faculty("GryffindorTest", "Red");
        Faculty created = restTemplate.postForEntity(url("/faculty"), faculty, Faculty.class).getBody();
        ResponseEntity<Faculty> response = restTemplate.getForEntity(url("/faculty/" + created.getId()), Faculty.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody().getName()).isEqualTo("GryffindorTest");
    }

    @Test
    @DisplayName("выдать все факультеты")
    void getAllFaculties() {
        restTemplate.postForEntity(url("/faculty"), new Faculty("GryffindorTest", "Red"), Faculty.class);
        restTemplate.postForEntity(url("/faculty"), new Faculty("GriffinsTest", "Red"), Faculty.class);
        ResponseEntity<Collection<Faculty>> response = restTemplate.exchange(url("/faculty"), //getForEntity() + .class от параметризованного типа не скомпилируется
                HttpMethod.GET, null, new ParameterizedTypeReference<>() {}); // ParameterizedTypeReference позволяет указать параметризованный тип (Collection<Faculty>) в рантайме

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotEmpty();
        Assertions.assertThat(response.getBody()).hasSize(2);
    }

    @Test
    @DisplayName("поиск факультета")
    void searchFacultyByNameOrColor() {
        restTemplate.postForEntity(url("/faculty"), new Faculty("CripsTest", "blue"), Faculty.class);
        ResponseEntity<Collection<Faculty>> response = restTemplate.exchange(url("/faculty/search?query=rip"),
                HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    @DisplayName("обновить факультет")
    void updateFaculty() {
        Faculty faculty = new Faculty("CarbonTest", "Black");
        Faculty created = restTemplate.postForEntity(url("/faculty"), faculty, Faculty.class).getBody();
        created.setColor("White");

        HttpEntity<Faculty> request = new HttpEntity<>(created);
        ResponseEntity<Faculty> response = restTemplate.exchange(url("/faculty"),
                HttpMethod.PUT, request, Faculty.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody().getColor()).isEqualTo("White");
    }

    @Test
    @DisplayName("удалить факультет")
    void deleteFaculty() {
        Faculty faculty = new Faculty("SmittTest", "Pink");
        Faculty created = restTemplate.postForEntity(url("/faculty"), faculty, Faculty.class).getBody();

        ResponseEntity<Faculty> response = restTemplate.exchange(url("/faculty/" + created.getId()),
                HttpMethod.DELETE, null, Faculty.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();
    }










}
