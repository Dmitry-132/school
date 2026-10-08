package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FacultyService {
    private final Map<Long, Faculty> facultyMap = new HashMap<>();
    private Long idCounter = 0L;

    public Faculty createFaculty(Faculty faculty) {
        faculty.setId(idCounter++);
        facultyMap.put(faculty.getId(), faculty);
        return faculty;
    }

    public Faculty getFaculty(Long id) {
        return facultyMap.get(id);
    }

    public Collection<Faculty> getAllFaculty() {
        return Collections.unmodifiableCollection(facultyMap.values());
    }

    public Collection<Faculty> getFacultyByColor(String color) {
        return facultyMap.values().stream().filter(s -> s.getColor().equalsIgnoreCase(color)).collect(Collectors.toList());
    }

    public Faculty updateFaculty(Faculty faculty) {
        if (!facultyMap.containsKey(faculty.getId())) {
            return null;
        }
        facultyMap.put(faculty.getId(), faculty);
        return faculty;
    }

    public Faculty deleteFaculty(Long id) {
        return facultyMap.remove(id);
    }
}
