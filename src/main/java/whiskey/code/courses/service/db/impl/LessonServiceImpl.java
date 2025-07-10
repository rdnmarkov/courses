package whiskey.code.courses.service.db.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import whiskey.code.courses.entity.Course;
import whiskey.code.courses.entity.Lesson;
import whiskey.code.courses.repository.CourseRepository;
import whiskey.code.courses.repository.LessonRepository;
import whiskey.code.courses.service.db.LessonService;

import java.util.Arrays;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;


    public Page<Lesson> findByPage(Long courseId, int offset){
        final int PAGE_SIZE = 10;
        var page = PageRequest.of(offset,PAGE_SIZE);
        return lessonRepository.findLessonsByCourse(courseId, page);
    }

    public Lesson findLesson(Long lessonId){
        return lessonRepository.findById(lessonId).get();
    }

    public Lesson createLesson(String command) {

        String[] parts = command.split("\\|");
        Optional<Course> course = courseRepository.findById(Long.parseLong(parts[1]));

        Lesson lesson = Lesson.builder()
                .title(parts[0])
                .course(course.get())
                .messageIds(Arrays.stream(parts[2].split(",")).map(Integer::parseInt).toList())
                .orderNumber(Integer.parseInt(parts[3]))
                .build();

        return lessonRepository.save(lesson);
    }

    public Lesson updateLesson(String command) {

        String[] parts = command.split("\\|");

        Optional<Course> course = courseRepository.findById(Long.parseLong(parts[2]));
        Lesson lesson = lessonRepository.findById(Long.parseLong(parts[0]))
                .orElseThrow(() -> new EntityNotFoundException("Lesson not found"));

        lesson.setTitle(parts[1]);
        lesson.setCourse(course.get());
        lesson.setMessageIds(Arrays.stream(parts[3].split(",")).map(Integer::parseInt).toList());
        lesson.setOrderNumber(Integer.parseInt(parts[4]));

        return lessonRepository.save(lesson);
    }

    public Lesson updateLessonTitle(String command) {
        String[] parts = command.split("\\|");

        Lesson lesson = lessonRepository.findById(Long.parseLong(parts[0]))
                .orElseThrow(() -> new EntityNotFoundException("Lesson not found"));

        lesson.setTitle(parts.length > 1 ? parts[1] : "");

        return lessonRepository.save(lesson);
    }

    public Lesson updateLessonCourse(String command) {
        String[] parts = command.split("\\|");

        Optional<Course> course = courseRepository.findById(Long.parseLong(parts[1]));

        Lesson lesson = lessonRepository.findById(Long.parseLong(parts[0]))
                .orElseThrow(() -> new EntityNotFoundException("Lesson not found"));

        lesson.setCourse(course.get());

        return lessonRepository.save(lesson);
    }

    public Lesson updateLessonMessage(String command) {
        String[] parts = command.split("\\|");

        Lesson lesson = lessonRepository.findById(Long.parseLong(parts[0]))
                .orElseThrow(() -> new EntityNotFoundException("Lesson not found"));

        lesson.setMessageIds(Arrays.stream(parts[1].split(",")).map(Integer::parseInt).toList());

        return lessonRepository.save(lesson);
    }

    public Lesson updateLessonOrderNumber(String command) {
        String[] parts = command.split("\\|");

        Lesson lesson = lessonRepository.findById(Long.parseLong(parts[0]))
                .orElseThrow(() -> new EntityNotFoundException("Lesson not found"));

        lesson.setOrderNumber(Integer.parseInt(parts[1]));

        return lessonRepository.save(lesson);
    }

    public void deleteLesson(String command) {
        lessonRepository.deleteById(Long.parseLong(command));
    }

    @Override
    public int getPercent(CallbackQuery callbackQuery, Integer curOrderNum) {

        String[] lessonsInfo = callbackQuery.getData().split("_");
        long courseId = Long.parseLong(lessonsInfo[2]);

        return (int) Math.round(lessonRepository.calculateCompletionPercentage(courseId, curOrderNum));
    }
}
