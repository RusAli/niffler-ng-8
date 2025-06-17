package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.entity.spend.CategoryEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CategoryDaoJDBC implements CategoryDao {

  private static final Config CFG = Config.getInstance();

  private final Connection connection;

  public CategoryDaoJDBC(Connection connection) {
    this.connection = connection;
  }

  @Override
  public CategoryEntity create(CategoryEntity category) {
    try (PreparedStatement ps = connection.prepareStatement(
            "INSERT INTO \"category\" (name, username,archived) " +
                    "VALUES (?,?,?)",
            PreparedStatement.RETURN_GENERATED_KEYS
    )) {

      ps.setString(1, category.getName());
      ps.setString(2, category.getUsername());
      ps.setBoolean(3, category.isArchived());

      ps.executeUpdate();

      final UUID generatedKey;
      try (ResultSet rs = ps.getGeneratedKeys()) {
        if (rs.next()) {
          generatedKey = rs.getObject("id", UUID.class);
        } else {
          throw new SQLException("Can't find id in ResultSet");
        }
      }

      category.setId(generatedKey);
      return category;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public CategoryEntity update(CategoryEntity category) {
    try (PreparedStatement ps = connection.prepareStatement(
            "UPDATE \"category\" SET name = ?, username = ?,archived = ? WHERE id = ?",
            PreparedStatement.RETURN_GENERATED_KEYS
    )) {

      ps.setString(1, category.getName());
      ps.setString(2, category.getUsername());
      ps.setBoolean(3, category.isArchived());
      ps.setObject(4, category.getId());

      ps.executeUpdate();
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
    return category;
  }

  @Override
  public Optional<CategoryEntity> findCategoryById(UUID id) {
    try (PreparedStatement ps = connection.prepareStatement(
            "SELECT * FROM \"category\" WHERE id = ?",
            PreparedStatement.RETURN_GENERATED_KEYS
    )) {
      ps.setObject(1, id);

      ps.executeQuery();

      try (ResultSet rs = ps.getResultSet()) {
        if (rs.next()) {
          CategoryEntity ce = new CategoryEntity();
          ce.setId(rs.getObject("id", UUID.class));
          ce.setName(rs.getString("name"));
          ce.setUsername(rs.getString("username"));
          ce.setArchived(rs.getBoolean("archived"));

          return Optional.of(ce);
        } else {
          return Optional.empty();
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String categoryName) {
    try (PreparedStatement ps = connection.prepareStatement(
            "SELECT * FROM \"category\" WHERE username = ? AND name = ?",
            PreparedStatement.RETURN_GENERATED_KEYS
    )) {
      ps.setString(1, username);
      ps.setString(2, categoryName);

      ps.execute();

      try (ResultSet rs = ps.getResultSet()) {
        if (rs.next()) {

          return Optional.of(getCategoryEntityFromResultSet(rs));

        } else {
          return Optional.empty();
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<CategoryEntity> findAllByUsername(String username) {
    try (PreparedStatement ps = connection.prepareStatement(
            "SELECT * FROM \"category\" WHERE username = ?",
            PreparedStatement.RETURN_GENERATED_KEYS
    )) {
      ps.setString(1, username);
      ps.execute();

      try (ResultSet rs = ps.getResultSet()) {
        List<CategoryEntity> categories = new ArrayList<>();
        while (rs.next()) {
          categories.add(getCategoryEntityFromResultSet(rs));
        }
        return categories;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<CategoryEntity> findAll() {
    try (PreparedStatement ps = connection.prepareStatement(
            "SELECT * FROM \"category\"",
            PreparedStatement.RETURN_GENERATED_KEYS
    )) {
      ps.execute();

      try (ResultSet rs = ps.getResultSet()) {
        List<CategoryEntity> categories = new ArrayList<>();
        while (rs.next()) {
          categories.add(getCategoryEntityFromResultSet(rs));
        }
        return categories;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void deleteCategory(CategoryEntity categoryEntity) {
    try (PreparedStatement ps = connection.prepareStatement(
            "DELETE FROM \"category\" WHERE id = ?",
            PreparedStatement.RETURN_GENERATED_KEYS
    )) {
      ps.setObject(1, categoryEntity.getId());
      ps.execute();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private CategoryEntity getCategoryEntityFromResultSet(ResultSet rs) throws SQLException {
    CategoryEntity category = new CategoryEntity();
    category.setId(rs.getObject("id", UUID.class));
    category.setName(rs.getString("name"));
    category.setUsername(rs.getString("username"));
    category.setArchived(rs.getBoolean("archived"));

    return category;
  }
}