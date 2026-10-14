package ru.hogwarts.school.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;
import ru.hogwarts.school.model.Student;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findByAge(int age);
    List<Student> findByAgeBetween(int min, int max);

    @Query("SELECT COUNT(s) FROM Student s") //В @Query пишется JPQL; для SQL нужно прописать value = "",nativeQuery = true но я вспомнил это уже когда все было готово
    Long countAllStudents();

    @Query("SELECT AVG(s.age) FROM Student s")
    Double getAverageAge();

    @Query("SELECT s FROM Student s ORDER BY s.id DESC")
    List<Student> findLastStudents(Pageable pageable);
}
