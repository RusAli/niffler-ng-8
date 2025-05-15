package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.CurrencyValues;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SpendDaoJDBC implements SpendDao {

  private static final Config CFG = Config.getInstance();

  @Override
  public SpendEntity create(SpendEntity spend) {
    try (Connection connection = Databases.connection(CFG.spendJdbcUrl())) {
      try (PreparedStatement ps = connection.prepareStatement(
              "INSERT INTO spend (username, spend_date, currency, amount, description, category_id) " +
                      "VALUES (?,?,?,?,?,?)",
              PreparedStatement.RETURN_GENERATED_KEYS
      )) {

        ps.setString(1, spend.getUsername());
        ps.setDate(2, spend.getSpendDate());
        ps.setString(3, spend.getCurrency().name());
        ps.setDouble(4, spend.getAmount());
        ps.setString(5, spend.getDescription());
        ps.setObject(6, spend.getCategory().getId());

        ps.executeUpdate();

        final UUID generatedKey;
        try (ResultSet rs = ps.getGeneratedKeys()) {
          if (rs.next()) {
            generatedKey = rs.getObject("id", UUID.class);
          } else {
            throw new SQLException("Can't find id in ResultSet");
          }
        }

        spend.setId(generatedKey);
        return spend;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<SpendEntity> findSpendById(UUID id) {
    try (Connection connection = Databases.connection(CFG.spendJdbcUrl())) {
      try (PreparedStatement ps = connection.prepareStatement(
              "SELECT * FROM spend WHERE id = ?",
              PreparedStatement.RETURN_GENERATED_KEYS
      )) {
        ps.setObject(1, id);

        ps.execute();

        try (ResultSet rs = ps.getResultSet()) {
          if (rs.next()) {

            return Optional.of(getSpendEntityFromResultSet(rs));

          } else {
            return Optional.empty();
          }
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }


  @Override
  public List<SpendEntity> findAllByUsername(String username) {
    try (Connection connection = Databases.connection(CFG.spendJdbcUrl())) {
      try (PreparedStatement ps = connection.prepareStatement(
              "SELECT * FROM spend WHERE username = ?",
              PreparedStatement.RETURN_GENERATED_KEYS
      )) {
        ps.setString(1, username);

        ps.execute();

        try (ResultSet rs = ps.getResultSet()) {
          List<SpendEntity> spends = new ArrayList<>();
          while (rs.next()) {
            spends.add(getSpendEntityFromResultSet(rs));
          }
          return spends;
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void deleteSpend(SpendEntity spend) {
    try (Connection connection = Databases.connection(CFG.spendJdbcUrl())) {
      try (PreparedStatement ps = connection.prepareStatement(
              "DELETE FROM spend WHERE id = ?",
              PreparedStatement.RETURN_GENERATED_KEYS
      )) {
        ps.setObject(1, spend.getId());
        ps.execute();
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private SpendEntity getSpendEntityFromResultSet(ResultSet rs) throws SQLException {

    SpendEntity spend = new SpendEntity();
    CategoryEntity category = new CategoryEntity();

    spend.setId(rs.getObject("id", UUID.class));
    spend.setUsername(rs.getString("username"));
    spend.setSpendDate(rs.getDate("spend_date"));
    spend.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
    spend.setAmount(rs.getDouble("amount"));
    spend.setDescription(rs.getString("description"));

    category.setId(rs.getObject("category_id", UUID.class));
    spend.setCategory(category);

    return spend;
  }
}