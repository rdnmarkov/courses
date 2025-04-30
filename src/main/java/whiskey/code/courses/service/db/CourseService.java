package whiskey.code.courses.service.db;

import whiskey.code.courses.entity.Course;

import java.util.List;

public interface CourseService {

    List<Course> findByVisibilityTrue();

}
