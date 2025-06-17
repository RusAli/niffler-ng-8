package guru.qa.niffler.data.mapper;

import guru.qa.niffler.data.entity.spend.CategoryEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class CategoryEntityRowMapper implements RowMapper<CategoryEntity> {

  public static final CategoryEntityRowMapper INSTANCE = new CategoryEntityRowMapper();

  private CategoryEntityRowMapper() {
  }

  @Override
  public CategoryEntity mapRow(ResultSet rs, int rowNum) throws SQLException {

    CategoryEntity categoryEntity = new CategoryEntity();

    categoryEntity.setId(rs.getObject("id", UUID.class));
    categoryEntity.setName(rs.getString("name"));
    categoryEntity.setUsername(rs.getString("username"));
    categoryEntity.setArchived(rs.getBoolean("archived"));

    return categoryEntity;
  }
}