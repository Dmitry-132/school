package ru.hogwarts.school;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(FacultyController.class)
public class FacultyControllerWithWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private tools.jackson.databind.ObjectMapper objectMapper;

    @MockitoBean
    private FacultyService facultyService;

    @Test
    @DisplayName("создать факультет")
    void createFaculty() throws Exception {
        Faculty faculty = new Faculty(1L, "Gryffindor", "Red");
        when(facultyService.createFaculty(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(post("/faculty")
                        .content(objectMapper.writeValueAsString(faculty))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Gryffindor"))
                .andExpect(jsonPath("$.color").value("Red"));
    }

    @Test
    @DisplayName("получить факультет")
    void getFaculty() throws Exception {
        Faculty faculty = new Faculty(1L, "Gry", "Red");
        when(facultyService.getFaculty(anyLong())).thenReturn(faculty);

        mockMvc.perform(get("/faculty/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Gry"));
    }

    @Test
    @DisplayName("получить все факультеты")
    void getAllFaculties() throws Exception {
        List<Faculty> faculties = List.of(
                new Faculty(1L, "Gryffindor", "Red"),
                new Faculty(2L, "GRU", "Reds"));
        when(facultyService.getAllFaculty()).thenReturn(faculties);

        mockMvc.perform(get("/faculty")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("получить факультет")
    void searchFaculty() throws Exception {
        List<Faculty> faculties = List.of(new Faculty(1L, "Gryffindor", "Red"));
        when(facultyService.findByNameOrColor(anyString())).thenReturn(faculties);

        mockMvc.perform(get("/faculty/search?query=grif")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @DisplayName("обновить факультет")
    void updateFaculty() throws Exception {
        Faculty faculty = new Faculty(1L, "Gryffindor", "Reds");
        when(facultyService.updateFaculty(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(put("/faculty")
                        .content(objectMapper.writeValueAsString(faculty))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.color").value("Reds"));
    }

    @Test
    @DisplayName("удалить факультет")
    void deleteFaculty() throws Exception {
        Faculty faculty = new Faculty(1L, "Gryffindor2", "colored");
        when(facultyService.deleteFaculty(anyLong())).thenReturn(faculty);

        mockMvc.perform(delete("/faculty/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
