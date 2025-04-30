package whiskey.code.courses.service.db.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import whiskey.code.courses.entity.Course;
import whiskey.code.courses.repository.CourseRepository;
import whiskey.code.courses.service.db.CourseService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    public List<Course> findByVisibilityTrue(){
        return courseRepository.findByVisibilityTrue();
    }
}
