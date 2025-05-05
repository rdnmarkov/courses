package whiskey.code.courses.service.db.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import whiskey.code.courses.entity.Lesson;
import whiskey.code.courses.repository.LessonRepository;
import whiskey.code.courses.service.db.LessonService;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;

    public Page<Lesson> findByPage(Long courseId, int offset){
        final int PAGE_SIZE = 10;
        var page = PageRequest.of(offset,PAGE_SIZE);
        return lessonRepository.findLessonsByCourse(courseId, page);
    }
}
