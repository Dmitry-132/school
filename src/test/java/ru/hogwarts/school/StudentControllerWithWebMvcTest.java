package ru.hogwarts.school;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(StudentController.class) // в () указан тестируемый контроллер
public class StudentControllerWithWebMvcTest { //MockMvc имитирует HTTP-запрос ВНУТРИ приложения, все зависимости(Service) — моки, грузит только затронутый контроллер, без Tomcat и БД

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private tools.jackson.databind.ObjectMapper objectMapper;

    @MockitoBean
    private StudentService studentService;

    @Test
    @DisplayName("создать студента")
    void createStudent() throws Exception {
        Student student = new Student(1L, "Harry", 10);
        when(studentService.createStudent(any(Student.class))).thenReturn(student);

        mockMvc.perform(post("/student")
                        .content(objectMapper.writeValueAsString(student))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Harry"))
                .andExpect(jsonPath("$.age").value(10));
    }

    @Test
    @DisplayName("получить студента")
    void getStudent() throws Exception {
        Student student = new Student(1L, "Ron", 11);
        when(studentService.getStudent(anyLong())).thenReturn(student);

        mockMvc.perform(get("/student/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Ron"));
    }

    @Test
    @DisplayName("получить всех студентов")
    void getAllStudents() throws Exception {
        List<Student> students = List.of(
                new Student(1L, "Harry", 11),
                new Student(2L, "Ron", 11));
        when(studentService.getAllStudents()).thenReturn(students);

        mockMvc.perform(get("/student")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("фильтр по возрасту")
    void getStudentsByAgeBetween() throws Exception {
        List<Student> students = List.of(
                new Student(1L, "Harry", 10),
                new Student(2L, "Ron", 15));
        when(studentService.getStudentsByAgeBetween(anyInt(), anyInt())).thenReturn(students);

        mockMvc.perform(get("/student/filterByAgeBetween?min=10&max=18")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("обновить студента")
    void updateStudent() throws Exception {
        Student student = new Student(1L, "Harry", 11);
        when(studentService.updateStudent(any(Student.class))).thenReturn(student);

        mockMvc.perform(put("/student")
                        .content(objectMapper.writeValueAsString(student))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.age").value(11));
    }

    @Test
    @DisplayName("удалить студента")
    void deleteStudent() throws Exception {
        Student student = new Student(1L, "Harry", 11);
        when(studentService.deleteStudent(anyLong())).thenReturn(student);

        mockMvc.perform(delete("/student/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
