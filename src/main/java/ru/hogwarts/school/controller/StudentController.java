package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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

    @GetMapping("/count")
    public Long getStudentsCount() {
        return studentService.getStudentsCount();
    }

    @GetMapping("/namesWith ")
    public List<String> getStudentNames(@RequestParam(defaultValue = "А") String letter) {
        return studentService.getStudentWhoseNamesStartsWith(letter);
    }

    @GetMapping("/averageAge")
    public Double getAverageAge() {
        return studentService.getAverageAge();
    }

    @GetMapping("/last") //?count=  или дефолт
    public List<Student> getLastStudents(@RequestParam(defaultValue = "5") int count) {
        return studentService.getLastStudents(count);
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
        Student deleted = studentService.deleteStudent(id);
        if (deleted == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(deleted);
    }

    @GetMapping("/print-parallel")
    public void printParallel() {
        List<Student> students = studentService.getAllStudents().stream().toList();
        if (students.size() < 6) {
            return;
        }
        System.out.println("0 " + students.get(0).getName());
        System.out.println("0 " + students.get(1).getName());

        Thread thread1 = new Thread(() -> {
            System.out.println("1 " + students.get(2).getName());
            System.out.println("1 " + students.get(3).getName());

        });

        Thread thread2 = new Thread(() -> {
            System.out.println("2 " + students.get(4).getName());
            System.out.println("2 " + students.get(5).getName());

        });
        thread1.start();
        thread2.start();
    }

    private final Object flag = new Object();

    @GetMapping("/print-synchronized")
    public void printParallelSynchronized() {
        List<Student> students = studentService.getAllStudents().stream().toList();
        if (students.size() < 6) {
            return;
        }
        Thread thread1 = new Thread(() -> {
            doOperaation(1, students.get(2));
            doOperaation(1, students.get(3));

        });
        Thread thread2 = new Thread(() -> {
            doOperaation(2, students.get(4));
            doOperaation(2, students.get(5));
        });
        thread1.start();
        thread2.start();
        doOperaation(0, students.get(0));
        doOperaation(0, students.get(1));
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("метод завершен");
    }

    private void doOperaation(int threads, Student student) {
        String name = student.getName().toUpperCase();
        try {
            Thread.sleep(4000); //метод с throws InterruptedException и он может быть прерван другим потоком типа thread.interrupt()
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Восстанавливаем флаг прерывания (best practice) и если честно я это вроде и понял но не совсем
            System.out.println("Поток был прерван: " + e.getMessage());
        }
        synchronized (flag) {
            System.out.println(threads + " " + name);
        }
    }


}
