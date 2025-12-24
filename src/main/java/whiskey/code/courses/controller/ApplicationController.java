package whiskey.code.courses.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import whiskey.code.courses.dto.CategoryDTO;
import whiskey.code.courses.dto.CourseWithLessonDTO;
import whiskey.code.courses.entity.Course;
import whiskey.code.courses.mapper.EntityDTOMapper;
import whiskey.code.courses.service.db.CategoryService;
import whiskey.code.courses.service.db.CourseService;
import whiskey.code.courses.service.handler.impl.WebAppHandlerImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@CrossOrigin(origins = "https://rdnmarkov.github.io")
public class ApplicationController {

    private final CategoryService categoryService;
    private final CourseService courseService;
    private final EntityDTOMapper dtoMapper;
    private final WebAppHandlerImpl webAppHandler;

    @GetMapping("/categories")
    public List<CategoryDTO> categories() {
        return categoryService.getCategories();
    }

    @GetMapping("/courses")
    public Map<String, Object> getCourses(
            @RequestParam(required = false) List<Long> categoryIds,
            @RequestParam(defaultValue = "0") int page
    ) {
        return courseService.getCourses(categoryIds,page);
    }

    @GetMapping("/search")
    public Map<String, Object> searchCourses(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page
    ) {
        Page<Course> courses = courseService.searchCourses(query, page);

        var coursesDTO = courses.get().map(dtoMapper::coursesToDto).toList();
        Map<String, Object> result = new HashMap<>();
        result.put("content", coursesDTO);
        result.put("totalPages", courses.getTotalPages());
        result.put("currentPage", courses.getNumber());
        return result;
    }


    @GetMapping("/course/{id}")
    public CourseWithLessonDTO getCourse(@PathVariable Long id) {

        return dtoMapper.coursesWithLessonToDto(courseService.findCourseBiId(id));
    }


    @PostMapping("/message/{messageId}")
    public ResponseEntity<Void> sendMessage(@PathVariable Integer messageId,
                                            @RequestParam Long chatId) {

        webAppHandler.handle(chatId,messageId);

        return ResponseEntity.ok().build();
    }
}
