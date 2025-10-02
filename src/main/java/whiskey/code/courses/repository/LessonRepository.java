package whiskey.code.courses.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import whiskey.code.courses.entity.Lesson;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

    @Query("SELECT l FROM Lesson l WHERE l.course.id = :courseId ORDER BY l.orderNumber ASC")
    Page<Lesson> findLessonsByCourse(@Param("courseId") Long courseId, Pageable pageable);

    @Query("""
            SELECT 
                (COUNT(l) * 100.0 / 
                (SELECT COUNT(l2) FROM Lesson l2 WHERE l2.course.id = :courseId))
            FROM Lesson l
            WHERE l.course.id = :courseId 
            AND l.orderNumber <= :currentOrderNumber
            """)
    Double calculateCompletionPercentage(
            @Param("courseId") Long courseId,
            @Param("currentOrderNumber") Integer currentOrderNumber
    );

}
