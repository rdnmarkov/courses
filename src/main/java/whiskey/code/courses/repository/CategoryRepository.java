package whiskey.code.courses.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import whiskey.code.courses.entity.Category;

import java.util.List;


@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByVisibilityTrue();

    Page<Category> findByVisibilityTrue(Pageable pageable);
}
