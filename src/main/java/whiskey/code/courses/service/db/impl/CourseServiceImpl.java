package whiskey.code.courses.service.db.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import whiskey.code.courses.entity.Course;
import whiskey.code.courses.mapper.EntityDTOMapper;
import whiskey.code.courses.repository.CourseRepository;
import whiskey.code.courses.service.db.CourseService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final EntityDTOMapper dtoMapper;
    private static final int PAGE_SIZE = 20;

    @Override
    public Map<String, Object> getCourses(List<Long> categoryIds, int offset) {

        if (categoryIds == null) {
            categoryIds = List.of();
        }

        var page = PageRequest.of(offset, PAGE_SIZE);

        Page<Course> courses;
        if (categoryIds.isEmpty()) {
            courses = courseRepository.findAll(page);
        } else {
            courses = courseRepository.findByCategory_IdInAndVisibilityTrue(categoryIds, page);
        }

        var coursesDTO = courses.get().map(dtoMapper::coursesToDto).toList();
        Map<String, Object> result = new HashMap<>();
        result.put("content", coursesDTO);
        result.put("totalPages", courses.getTotalPages());
        result.put("currentPage", courses.getNumber());

        return result;
    }

    @Override
    public Page<Course> searchCourses(String keyword, int page) {
        var pageable = PageRequest.of(page, PAGE_SIZE);
        return courseRepository.searchByCourseOrLessonTitle(keyword, pageable);
    }

    @Override
    public Course findCourseBiId(Long id) {
        return courseRepository.findById(id).get();
    }

    public Page<Course> findByVisibilityTruePage(int offset, Long categoryId) {
        final int PAGE_SIZE = 10;
        var page = PageRequest.of(offset, PAGE_SIZE);
        return courseRepository.findByVisibilityTrue(page);
    }

    public Course createCourse(String command) {

        String[] parts = command.split("\\|");

        Course course = Course.builder()
                .title(parts[0])
                .visibility(parts.length > 1 && Boolean.parseBoolean(parts[1]))
                .build();

        return courseRepository.save(course);
    }

    public Course updateCourse(String command) {
        String[] parts = command.split("\\|");

        Course course = courseRepository.findById(Long.parseLong(parts[0]))
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        course.setTitle(parts.length > 1 ? parts[1] : "");
        course.setVisibility(parts.length > 1 && Boolean.parseBoolean(parts[2]));

        return courseRepository.save(course);
    }

    public Course updateCourseTitle(String command) {

        String[] parts = command.split("\\|");

        Course course = courseRepository.findById(Long.parseLong(parts[0]))
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        course.setTitle(parts.length > 1 ? parts[1] : "");

        return courseRepository.save(course);
    }

    public Course updateCourseVis(String command) {

        String[] parts = command.split("\\|");

        Course course = courseRepository.findById(Long.parseLong(parts[0]))
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        course.setVisibility(parts.length > 1 && Boolean.parseBoolean(parts[1]));

        return courseRepository.save(course);
    }

    public void deleteCourse(String command) {
        courseRepository.deleteById(Long.parseLong(command));
    }
}
