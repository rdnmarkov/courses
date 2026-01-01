package whiskey.code.courses.mapper;


import org.springframework.stereotype.Component;
import whiskey.code.courses.dto.CategoryDTO;
import whiskey.code.courses.dto.CourseDTO;
import whiskey.code.courses.dto.CourseWithLessonDTO;
import whiskey.code.courses.dto.LessonDTO;
import whiskey.code.courses.entity.Category;
import whiskey.code.courses.entity.Course;
import whiskey.code.courses.entity.Lesson;

@Component
public class EntityDTOMapper {

    public CategoryDTO categoryToDto(Category c) {
        return new CategoryDTO(c.getId(), c.getTitle(), c.getDescription(), c.isVisibility());
    }

    public CourseDTO coursesToDto(Course c) {
        return new CourseDTO(c.getId(), c.getTitle(), c.getDescription(), c.isVisibility());
    }

    public CourseWithLessonDTO coursesWithLessonToDto(Course c) {

        return new CourseWithLessonDTO(c.getId(),
                c.getTitle(),
                c.getDescription(),
                c.isVisibility(),
                c.getCategory().getId(),
                c.getCategory().getTitle(),
                c.getLessons().stream().map(this::lessonToDto).toList());
    }

    private LessonDTO lessonToDto(Lesson l) {
        return new LessonDTO(l.getId(),
                l.getTitle(),
                l.getDescription(),
                l.getOrderNumber(),
                l.getMessageIds());
    }
}
