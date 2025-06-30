package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.holder;

public class AuthUserDaoJDBC implements AuthUserDao {

  private static final Config CFG = Config.getInstance();

  private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();


  @Override
  public AuthUserEntity createUser(AuthUserEntity authUserEntity) {
    try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
            "INSERT INTO \"user\" (username, password, enabled,account_non_expired,account_non_locked,credentials_non_expired) " +
                    "VALUES (?,?,?,?,?,?)",
            PreparedStatement.RETURN_GENERATED_KEYS
    )) {

      ps.setString(1, authUserEntity.getUsername());
      ps.setString(2, pe.encode(authUserEntity.getPassword()));
      ps.setBoolean(3, authUserEntity.getEnabled());
      ps.setBoolean(4, authUserEntity.getAccountNonExpired());
      ps.setBoolean(5, authUserEntity.getAccountNonLocked());
      ps.setBoolean(6, authUserEntity.getCredentialsNonExpired());

      ps.executeUpdate();

      final UUID generatedKey;
      try (ResultSet rs = ps.getGeneratedKeys()) {
        if (rs.next()) {
          generatedKey = rs.getObject("id", UUID.class);
        } else {
          throw new SQLException("Can't find id in ResultSet");
        }
      }

      authUserEntity.setId(generatedKey);
      return authUserEntity;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<AuthUserEntity> findById(UUID id) {
    try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
            "SELECT * FROM \"user\" WHERE id = ?"
    )) {
      ps.setObject(1, id);
      ps.execute();

      try (ResultSet rs = ps.getResultSet()) {
        if (rs.next()) {

          return Optional.of(getAuthUserEntityFromResultSet(rs));

        } else {
          return Optional.empty();
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<AuthUserEntity> findByUsername(String username) {
    try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
            "SELECT * FROM \"user\" WHERE username = ?"
    )) {
      ps.setObject(1, username);
      ps.execute();

      try (ResultSet rs = ps.getResultSet()) {
        if (rs.next()) {

          return Optional.of(getAuthUserEntityFromResultSet(rs));

        } else {
          return Optional.empty();
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }


  private AuthUserEntity getAuthUserEntityFromResultSet(ResultSet rs) throws SQLException {

    AuthUserEntity authUserEntity = new AuthUserEntity();

    authUserEntity.setId(rs.getObject("id", UUID.class));
    authUserEntity.setUsername(rs.getString("username"));
    authUserEntity.setPassword(rs.getString("password"));
    authUserEntity.setEnabled(rs.getBoolean("enabled"));
    authUserEntity.setAccountNonExpired(rs.getBoolean("account_non_expired"));
    authUserEntity.setAccountNonLocked(rs.getBoolean("account_non_locked"));
    authUserEntity.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));
    return authUserEntity;
  }
}