package whiskey.code.courses.service.db.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import whiskey.code.courses.entity.Course;
import whiskey.code.courses.repository.CourseRepository;
import whiskey.code.courses.service.db.CourseService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    public Page<Course> findByVisibilityTruePage(int offset){
        final int PAGE_SIZE = 10;
        var page = PageRequest.of(offset,PAGE_SIZE);
        return courseRepository.findByVisibilityTrue(page);
    }
}
