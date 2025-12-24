package whiskey.code.courses.service.db;

import org.springframework.data.domain.Page;
import whiskey.code.courses.entity.Category;

import java.util.List;

public interface CategoryService {

    List<whiskey.code.courses.dto.CategoryDTO> getCategories();

    Page<Category> findByVisibilityTruePage(int offset);
}
