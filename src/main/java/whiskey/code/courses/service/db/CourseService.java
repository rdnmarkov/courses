package whiskey.code.courses.service.db;

import org.springframework.data.domain.Page;
import whiskey.code.courses.entity.Course;

import java.util.List;

public interface CourseService {

    Page<Course> findByVisibilityTruePage(int offset);

}
