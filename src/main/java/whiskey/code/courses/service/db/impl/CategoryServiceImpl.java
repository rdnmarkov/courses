package whiskey.code.courses.service.db.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import whiskey.code.courses.dto.CategoryDTO;
import whiskey.code.courses.entity.Category;
import whiskey.code.courses.entity.Course;
import whiskey.code.courses.mapper.EntityDTOMapper;
import whiskey.code.courses.repository.CategoryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements whiskey.code.courses.service.db.CategoryService {

    private final CategoryRepository categoryRepository;
    private final EntityDTOMapper dtoMapper;

    public List<CategoryDTO> getCategories() {
        return categoryRepository.findByVisibilityTrue().stream().map(dtoMapper::categoryToDto).toList();
    }

    @Override
    public Page<Category> findByVisibilityTruePage(int offset) {
        final int PAGE_SIZE = 10;
        var page = PageRequest.of(offset, PAGE_SIZE);
        return categoryRepository.findByVisibilityTrue(page);
    }


}
