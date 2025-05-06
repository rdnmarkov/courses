package whiskey.code.courses.service.db;

import org.springframework.data.domain.Page;
import whiskey.code.courses.entity.Lesson;

public interface LessonService {

    Page<Lesson> findByPage(Long courseId, int offset);

    Lesson findLesson(Long lessonId);

    Lesson createLesson(String command);

    Lesson updateLesson(String command);

    Lesson updateLessonTitle(String command);

    Lesson updateLessonCourse(String command);

    Lesson updateLessonMessage(String command);

    Lesson updateLessonOrderNumber(String command);

    void deleteLesson(String command);

}
