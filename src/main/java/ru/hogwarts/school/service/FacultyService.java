package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.FacultyRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FacultyService {

    private static final Logger logger = LoggerFactory.getLogger(FacultyService.class);

    private final FacultyRepository facultyRepository;

    @Autowired     //вроде нужен только при нескольких конструкторах, но пусть будет
    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty createFaculty(Faculty faculty) {
        logger.info("Was invoked method for create faculty");
        return facultyRepository.save(faculty);
    }

    public Faculty getFaculty(Long id) {
        logger.info("Was invoked method for get faculty by id = {}", id);

        Faculty faculty = facultyRepository.findById(id).orElse(null);
        if (faculty == null) {
            logger.error("get: Faculty with id = {} not found", id);
        }
        return faculty;
    }

    public Collection<Faculty> getAllFaculty() {
        logger.info("Was invoked method for get all faculties");
        return facultyRepository.findAll();
    }

    public Collection<Faculty> getFacultyByColor(String color) {
        logger.info("Was invoked method for get faculties by color = {}", color);
        Collection<Faculty> faculties = facultyRepository.findByColor(color);
        if (faculties.isEmpty()) {
            logger.warn("faculty with color = {} not found", color);
        }
        return faculties;
    }

    public Faculty updateFaculty(Faculty faculty) {
        logger.info("Was invoked method for update faculty with id = {}", faculty.getId());
        if (!facultyRepository.existsById(faculty.getId())) {
            logger.error("update: Faculty with id = {} not found", faculty.getId());
            return null;
        }
        return facultyRepository.save(faculty);
    }

    public Faculty deleteFaculty(Long id) {
        logger.info("Was invoked method for delete faculty with id = {}", id);
           Faculty faculty = facultyRepository.findById(id).orElse(null);
        if (faculty == null) {
            logger.error("delete: faculty with id = {} not found", id);
            return null;
        }
           facultyRepository.deleteById(id);
           return faculty;
    }

    public Collection<Faculty> findByNameOrColor(String query) { //принимает два параметра,ищем по одному шаблну
        logger.info("Was invoked method for find faculties by name or color = {}", query);
        Collection<Faculty> faculties = facultyRepository.findByNameContainingIgnoreCaseOrColorContainingIgnoreCase(query, query);
        if (faculties.isEmpty()) {
            logger.warn("faculty with name or color = {} not found", query);
        }
        return faculties;
    }
}
