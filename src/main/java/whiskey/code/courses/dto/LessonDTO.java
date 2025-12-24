package whiskey.code.courses.dto;

import java.util.List;

public record LessonDTO(Long id, String title, String description, Integer orderNumber, List<Integer> messageIds) {}
