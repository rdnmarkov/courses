package whiskey.code.courses.dto;

import java.util.List;

public record CourseWithLessonDTO(
        Long id,
        String title,
        String description,
        boolean visibility,
        Long categoryId,
        String categoryTitle,
        List<LessonDTO> lessons) {
}