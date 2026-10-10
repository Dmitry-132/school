package ru.hogwarts.school.service;

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

    private final FacultyRepository facultyRepository;

    @Autowired     //вроде нужен только при нескольких конструкторах, но пусть будет
    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty createFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public Faculty getFaculty(Long id) {
        return facultyRepository.findById(id).get();
    }

    public Collection<Faculty> getAllFaculty() {
        return facultyRepository.findAll();
    }

    public Collection<Faculty> getFacultyByColor(String color) {
        return facultyRepository.findByColor(color);
    }

    public Faculty updateFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public void deleteFaculty(Long id) {
            facultyRepository.deleteById(id);
    }

    public Collection<Faculty> findByNameOrColor(String query) { //принимает два параметра,ищем по одному шаблну
        return facultyRepository.findByNameContainingIgnoreCaseOrColorContainingIgnoreCase(query, query);
    }
}
