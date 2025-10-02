package whiskey.code.courses.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import whiskey.code.courses.entity.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    Page<Course> findByVisibilityTrue(Pageable pageable);
}
