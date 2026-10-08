package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final Map<Long, Student> studentMap = new HashMap<>();
    private Long idCounter = 0L;

    public Student createStudent(Student student) {
        student.setId(idCounter++);
        studentMap.put(student.getId(), student);
        return student;
    }

    public Student getStudent(Long id) {
        return studentMap.get(id);
    }

    public Collection<Student> getAllStudents() {
        return Collections.unmodifiableCollection(studentMap.values());
    }

    public Collection<Student> getStudentsByAge(int age) {
        return studentMap.values().stream().filter(s -> s.getAge() == age).collect(Collectors.toList());
    }

    public Student updateStudent(Student student) {
        if (!studentMap.containsKey(student.getId())) {
            return null;
        }
        studentMap.put(student.getId(), student);
        return student;
    }

    public Student deleteStudent(Long id) {
        return studentMap.remove(id);
    }

}
