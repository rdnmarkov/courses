package whiskey.code.courses.service.db;

import org.springframework.data.domain.Page;
import whiskey.code.courses.entity.Course;

import java.util.List;
import java.util.Map;

public interface CourseService {

    Map<String, Object> getCourses(List<Long> categoryIds, int offset);

    Page<Course> searchCourses(String keyword, int page);

    Course findCourseBiId(Long id);

    Page<Course> findByVisibilityTruePage(int offset, Long categoryId);

    Course createCourse(String command);

    Course updateCourse(String command);

    Course updateCourseTitle(String command);

    Course updateCourseVis(String command);

    void deleteCourse(String command);
}
