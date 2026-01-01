package whiskey.code.courses.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import whiskey.code.courses.entity.Course;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    Page<Course> findByVisibilityTrue(Pageable pageable);

    Page<Course> findAll(Pageable pageable);

    @Query("""
            SELECT DISTINCT c FROM Course c
            LEFT JOIN c.lessons l
            WHERE c.visibility = true
              AND (
                  LOWER(c.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
                  OR LOWER(l.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
              )
            """)
    Page<Course> searchByCourseOrLessonTitle(@Param("keyword") String keyword, Pageable pageable);

    @Query("""
            SELECT DISTINCT c FROM Course c
            LEFT JOIN c.lessons l
            WHERE c.visibility = true
              AND (
                  LOWER(c.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
                  OR LOWER(l.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
              )
            """)
    List<Course> searchByCourseOrLessonTitle(@Param("keyword") String keyword);

    Page<Course> findByCategory_IdInAndVisibilityTrue(List<Long> categoryIds, Pageable pageable);

}
