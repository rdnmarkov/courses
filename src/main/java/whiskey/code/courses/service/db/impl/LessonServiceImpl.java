package whiskey.code.courses.service.db.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import whiskey.code.courses.repository.LessonRepository;
import whiskey.code.courses.service.db.LessonService;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;

    
}
