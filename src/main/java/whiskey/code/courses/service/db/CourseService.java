package whiskey.code.courses.service.db;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import whiskey.code.courses.entity.Course;

import java.util.List;

public interface CourseService {

    Page<Course> findByVisibilityTruePage(int offset);

    Course createCourse(String command);

    Course updateCourse(String command);

    Course updateCourseTitle(String command);

    Course updateCourseVis(String command);

    void deleteCourse(String command);
}
