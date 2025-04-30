package whiskey.code.courses.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import whiskey.code.courses.entity.Course;
import whiskey.code.courses.entity.Lesson;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
}
