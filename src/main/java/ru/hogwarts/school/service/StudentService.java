package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.StudentRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private static final long parallelStreamStarts = 5;

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Long getStudentsCount() {
        logger.info("Was invoked method for get students count");
        return studentRepository.countAllStudents();
    }

    public Double getAverageAge() {
        logger.info("Was invoked method for get average age");
        List<Student> students = studentRepository.findAll();
        if (students.size() > parallelStreamStarts) {
            return students.parallelStream()
                    .mapToInt(Student::getAge)
                    .average()
                    .orElse(0);
        }
        return students.stream()
                .mapToInt(Student::getAge)
                .average()
                .orElse(0);

    }

    public List<Student> getLastStudents(int count) {
        logger.info("Was invoked method for get last {} students", count); // при простом ("id = " + id) сама строка создается всегда, несмотря на уровень логирования
        List<Student> students = studentRepository.findLastStudents(PageRequest.of(0, count));
        if (students.size() < count) {
            logger.warn(" {} is more Students than the total amount", count);
        }
        return students;
    }

    public Student createStudent(Student student) {
        logger.info("Was invoked method for create student");
        return studentRepository.save(student);
    }

    public Student getStudent(Long id) {
        logger.info("Was invoked method for get student by id = {}", id);
        Student student = studentRepository.findById(id).orElse(null);
        if (student == null) {
            logger.error("getStudent: Student with id = {} not found", id);
        }
        return student;
    }

    public Collection<Student> getAllStudents() {
        logger.info("Was invoked method for get all students");
        return studentRepository.findAll();
    }

    public List<String> getStudentWhoseNamesStartsWith(String letter) {
        logger.info("Was invoked method for get student names starts with '{}'", letter);
        List<Student> students = studentRepository.findAll();

        if (students.size() > parallelStreamStarts) {
            return students.parallelStream()
                    .map(Student::getName)
                    .filter(name -> name.startsWith(letter))
                    .map(String::toUpperCase)
                    .sorted() // последовательная операция
//                    .sequential() // Дальнейшие методы, выполняемые параллельно, выполнятся последовательно без разделения
                    .collect(Collectors.toList());
        }
        return students.stream()
                .map(Student::getName)
                .filter(name -> name.startsWith(letter))
                .map(String::toUpperCase)
                .sorted()
                .collect(Collectors.toList());

    }

    public Collection<Student> getStudentsByAge(int age) {
        logger.info("Was invoked method for get students by age = {}", age);
        Collection<Student> students = studentRepository.findByAge(age);
        if (students.isEmpty()) {
            logger.warn("Student aged {} years not found", age);
        }
        return students;
    }

    public Collection<Student> getStudentsByAgeBetween(int min, int max) {
        logger.info("Was invoked method for get students by age between {} and {}", min, max);
        Collection<Student> students = studentRepository.findByAgeBetween(min, max);
        if (students.isEmpty()) {
            logger.warn("Student aged {} to {} years not found", min, max);
        }
        return students;
    }

    public Student updateStudent(Student student) {
        logger.info("Was invoked method for update student with id = {}", student.getId());
        if (!studentRepository.existsById(student.getId())) {
            logger.error("updateStudent: Student with id = {} not found", student.getId());
            return null;
        }
        return studentRepository.save(student);
    }

    public Student deleteStudent(Long id) {
        logger.info("Was invoked method for delete student with id = {}", id);
        Student student = studentRepository.findById(id).orElse(null);
        if (student == null) {
            logger.error("Cannot delete: student with id = {} not found", id);
            return null;
        }
        studentRepository.deleteById(id);
        return student;
    }
}
