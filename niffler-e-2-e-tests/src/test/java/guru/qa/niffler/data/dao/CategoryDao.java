package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.spend.CategoryEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryDao {

  CategoryEntity create(CategoryEntity categoryEntity);

  CategoryEntity update(CategoryEntity categoryEntity);

  Optional<CategoryEntity> findCategoryById(UUID id);

  Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String categoryName);

  List<CategoryEntity> findAllByUsername(String username);

  List<CategoryEntity> findAll();

  void deleteCategory(CategoryEntity categoryEntity);
}