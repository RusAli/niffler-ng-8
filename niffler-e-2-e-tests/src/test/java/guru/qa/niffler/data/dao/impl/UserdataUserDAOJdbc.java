package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.UserDao;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.model.CurrencyValues;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class UserdataUserDAOJdbc implements UserDao {

  private static final Config CFG = Config.getInstance();

  private final Connection connection;

  public UserdataUserDAOJdbc(Connection connection) {
    this.connection = connection;
  }

  @Override
  public UserEntity createUser(UserEntity userEntity) {
    try (PreparedStatement ps = connection.prepareStatement(
            "INSERT INTO user (username,currency,firstname,surname,full_name,photo,photo_small) " +
                    "VALUES (?,?,?,?,?,?,?)",
            PreparedStatement.RETURN_GENERATED_KEYS
    )) {

      ps.setString(1, userEntity.getSurname());
      ps.setString(2, userEntity.getCurrency().name());
      ps.setString(3, userEntity.getFirstname());
      ps.setString(4, userEntity.getSurname());
      ps.setString(5, userEntity.getFullname());
      ps.setBytes(6, userEntity.getPhoto());
      ps.setBytes(7, userEntity.getPhotoSmall());

      ps.executeUpdate();

      final UUID generatedKey;
      try (ResultSet rs = ps.getGeneratedKeys()) {
        if (rs.next()) {
          generatedKey = rs.getObject("id", UUID.class);
        } else {
          throw new SQLException("Can't find id in ResultSet");
        }
      }

      userEntity.setId(generatedKey);
      return userEntity;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<UserEntity> findById(UUID id) {
    try (PreparedStatement ps = connection.prepareStatement(
            "SELECT * FROM user WHERE id = ?",
            PreparedStatement.RETURN_GENERATED_KEYS
    )) {
      ps.setObject(1, id);

      ps.execute();

      try (ResultSet rs = ps.getResultSet()) {
        if (rs.next()) {

          return Optional.of(getUserEntityFromResultSet(rs));

        } else {
          return Optional.empty();
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<UserEntity> findByUsername(String username) {
    try (PreparedStatement ps = connection.prepareStatement(
            "SELECT * FROM user WHERE username = ?",
            PreparedStatement.RETURN_GENERATED_KEYS
    )) {
      ps.setString(1, username);

      ps.execute();

      try (ResultSet rs = ps.getResultSet()) {
        if (rs.next()) {

          return Optional.of(getUserEntityFromResultSet(rs));

        } else {
          return Optional.empty();
        }
      }
    } catch (
            SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void delete(UserEntity userEntity) {
    try (PreparedStatement ps = connection.prepareStatement(
            "DELETE FROM user WHERE id = ?",
            PreparedStatement.RETURN_GENERATED_KEYS
    )) {
      ps.setObject(1, userEntity.getId());
      ps.execute();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private UserEntity getUserEntityFromResultSet(ResultSet rs) throws SQLException {
    UserEntity userEntity = new UserEntity();

    userEntity.setId(rs.getObject("id", UUID.class));
    userEntity.setUsername(rs.getString("username"));
    userEntity.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
    userEntity.setFirstname(rs.getString("firstname"));
    userEntity.setSurname(rs.getString("surname"));
    userEntity.setFullname(rs.getString("full_name"));
    userEntity.setPhoto(rs.getBytes("photo"));
    userEntity.setPhotoSmall(rs.getBytes("photo_small"));

    return userEntity;
  }
}
