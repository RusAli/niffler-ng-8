package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.mapper.AuthUserEntityRowMapper;
import guru.qa.niffler.data.repository.AuthUserRepository;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.holder;

public class AuthUserRepositoryJDBC implements AuthUserRepository {

  private static final Config CFG = Config.getInstance();

  private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();


  @Override
  public AuthUserEntity createUser(AuthUserEntity authUserEntity) {
    try (PreparedStatement userPs = holder(CFG.authJdbcUrl()).connection().prepareStatement(
            "INSERT INTO \"user\" (username, password, enabled,account_non_expired,account_non_locked,credentials_non_expired) " +
                    "VALUES (?,?,?,?,?,?)",
            PreparedStatement.RETURN_GENERATED_KEYS);
         PreparedStatement authorityPs = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                 "INSERT INTO \"authority\" (user_id, authority) VALUES (?,?)")
    ) {

      userPs.setString(1, authUserEntity.getUsername());
      userPs.setString(2, pe.encode(authUserEntity.getPassword()));
      userPs.setBoolean(3, authUserEntity.getEnabled());
      userPs.setBoolean(4, authUserEntity.getAccountNonExpired());
      userPs.setBoolean(5, authUserEntity.getAccountNonLocked());
      userPs.setBoolean(6, authUserEntity.getCredentialsNonExpired());

      userPs.executeUpdate();

      final UUID generatedKey;
      try (ResultSet rs = userPs.getGeneratedKeys()) {
        if (rs.next()) {
          generatedKey = rs.getObject("id", UUID.class);
        } else {
          throw new SQLException("Can't find id in ResultSet");
        }
      }

      authUserEntity.setId(generatedKey);

      for (AuthorityEntity authority : authUserEntity.getAuthorities()) {

        authorityPs.setObject(1, generatedKey);
        authorityPs.setString(2, authority.getAuthority().name());
        authorityPs.addBatch();
        authorityPs.clearParameters();
      }

      authorityPs.executeBatch();

      return authUserEntity;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<AuthUserEntity> findById(UUID id) {
    try (PreparedStatement userPs = holder(CFG.authJdbcUrl()).connection().prepareStatement(
            "select * from \"user\" u join authority a on u.id = a.user_id where u.id = ?"
    )) {
      userPs.setObject(1, id);
      userPs.execute();

      try (ResultSet rs = userPs.getResultSet()) {

        AuthUserEntity user = null;
        List<AuthorityEntity> authorityEntities = new ArrayList<>();
        while (rs.next()) {

          if (user == null) {
            user = AuthUserEntityRowMapper.INSTANCE.mapRow(rs, 1);
          }

          AuthorityEntity ae = new AuthorityEntity();
          ae.setUser(user);
          ae.setId(rs.getObject("a.id", UUID.class));
          ae.setAuthority(Authority.valueOf(rs.getString("authority")));
          authorityEntities.add(ae);

          AuthUserEntity result = new AuthUserEntity();

          result.setId(rs.getObject("id", UUID.class));
          result.setUsername(rs.getString("username"));
          result.setPassword(rs.getString("password"));
          result.setEnabled(rs.getBoolean("enabled"));
          result.setAccountNonExpired(rs.getBoolean("account_non_expired"));
          result.setAccountNonLocked(rs.getBoolean("account_non_locked"));
          result.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));
        }
        if (user == null) {
          return Optional.empty();
        } else {
          user.setAuthorities(authorityEntities);
          return Optional.of(user);
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<AuthUserEntity> findByUsername(String username) {
    try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
            "select * from \"user\" u join public.authority a on u.id = a.user_id where u.id = ?"
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