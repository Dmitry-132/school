package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;

@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudent(@PathVariable Long id) {
        Student student = studentService.getStudent(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @GetMapping("/{id}/faculty")
    public ResponseEntity<Faculty> getStudentFaculty(@PathVariable Long id) {
        Student student = studentService.getStudent(id);
        if (student == null || student.getFaculty() == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student.getFaculty());
    }

    @GetMapping
    public Collection<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/filterByAge")
    public Collection<Student> getStudentsByAge(@RequestParam int age) {
        return studentService.getStudentsByAge(age);
    }

    @GetMapping("/filterByAgeBetween") //можно объединить. но пока нет времени (2 ролик 0:30)
    public Collection<Student> getStudentsByAgeBetween(@RequestParam int min,
                                                       @RequestParam int max) {
        return studentService.getStudentsByAgeBetween(min, max);
    }

    @PutMapping
    public ResponseEntity<Student> updateStudent(@RequestBody Student student) {
        Student updated = studentService.updateStudent(student);
        if (updated == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Student> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }

}
