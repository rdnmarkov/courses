package whiskey.code.courses.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import whiskey.code.courses.entity.Course;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("SELECT c FROM Course c JOIN FETCH c.lessons") // Загружает курсы с уроками за один запрос
    List<Course> findAllCoursesWithLessons();

    Page<Course> findByVisibilityTrue(Pageable pageable);
}
