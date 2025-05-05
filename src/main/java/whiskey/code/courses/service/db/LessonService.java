package whiskey.code.courses.service.db;

import org.springframework.data.domain.Page;
import whiskey.code.courses.entity.Lesson;

public interface LessonService {

    Page<Lesson> findByPage(Long courseId,int offset);

}
