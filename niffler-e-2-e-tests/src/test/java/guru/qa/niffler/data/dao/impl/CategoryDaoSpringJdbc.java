package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.mapper.CategoryEntityRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CategoryDaoSpringJdbc implements CategoryDao {

  private final DataSource dataSource;

  public CategoryDaoSpringJdbc(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public CategoryEntity create(CategoryEntity categoryEntity) {

    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    KeyHolder kh = new GeneratedKeyHolder();

    jdbcTemplate.update(
            con -> {

              PreparedStatement ps = con.prepareStatement(
                      "INSERT INTO \"category\" (name, username,archived) VALUES (?,?,?)",
                      PreparedStatement.RETURN_GENERATED_KEYS);

              ps.setString(1, categoryEntity.getName());
              ps.setString(2, categoryEntity.getUsername());
              ps.setBoolean(3, categoryEntity.isArchived());

              return ps;
            }, kh
    );

    final UUID generatedKey = (UUID) kh.getKeys().get("id");
    categoryEntity.setId(generatedKey);

    return categoryEntity;
  }

  @Override
  public CategoryEntity update(CategoryEntity categoryEntity) {

    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

    jdbcTemplate.update(
            con -> {

              PreparedStatement ps = con.prepareStatement(
                      "UPDATE \"category\" SET name = ?, username = ?,archived = ? WHERE id = ?");

              ps.setString(1, categoryEntity.getName());
              ps.setString(2, categoryEntity.getUsername());
              ps.setBoolean(3, categoryEntity.isArchived());
              ps.setObject(4, categoryEntity.getId());

              return ps;
            }
    );

    return categoryEntity;
  }

  @Override
  public Optional<CategoryEntity> findCategoryById(UUID id) {

    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    return Optional.ofNullable(
            jdbcTemplate.queryForObject(
                    "SELECT * FROM \"category\" WHERE id = ?",
                    CategoryEntityRowMapper.INSTANCE,
                    id
            )
    );
  }

  @Override
  public Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String categoryName) {

    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    return Optional.ofNullable(
            jdbcTemplate.queryForObject(
                    "SELECT * FROM \"category\" WHERE username = ? AND name = ?",
                    CategoryEntityRowMapper.INSTANCE,
                    username,
                    categoryName
            )
    );
  }

  @Override
  public List<CategoryEntity> findAllByUsername(String username) {

    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    return jdbcTemplate.query(
            "SELECT * FROM  \"category\" WHERE username = ?",
            CategoryEntityRowMapper.INSTANCE,
            username
    );
  }

  @Override
  public List<CategoryEntity> findAll() {

    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    return jdbcTemplate.query(
            "SELECT * FROM  \"category\"",
            CategoryEntityRowMapper.INSTANCE
    );
  }

  @Override
  public void deleteCategory(CategoryEntity categoryEntity) {

    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    jdbcTemplate.update(
            "DELETE FROM \"category\" WHERE id = ?",
            categoryEntity.getId()
    );
  }
}