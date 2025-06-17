package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.mapper.AuthUserEntityRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.Optional;
import java.util.UUID;

public class AuthUserDaoSpringJdbc implements AuthUserDao {

  private final DataSource dataSource;
  private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

  public AuthUserDaoSpringJdbc(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public AuthUserEntity createUser(AuthUserEntity authUserEntity) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    KeyHolder kh = new GeneratedKeyHolder();

    jdbcTemplate.update(
            con -> {

              PreparedStatement ps = con.prepareStatement(
                      "INSERT INTO \"user\" (username, password, enabled,account_non_expired,account_non_locked,credentials_non_expired) " +
                              "VALUES (?,?,?,?,?,?)",
                      PreparedStatement.RETURN_GENERATED_KEYS);

              ps.setString(1, authUserEntity.getUsername());
              ps.setString(2, pe.encode(authUserEntity.getPassword()));
              ps.setBoolean(3, authUserEntity.getEnabled());
              ps.setBoolean(4, authUserEntity.getAccountNonExpired());
              ps.setBoolean(5, authUserEntity.getAccountNonLocked());
              ps.setBoolean(6, authUserEntity.getCredentialsNonExpired());

              return ps;
            }, kh
    );

    final UUID generatedKey = (UUID) kh.getKeys().get("id");
    authUserEntity.setId(generatedKey);

    return authUserEntity;
  }

  @Override
  public Optional<AuthUserEntity> findById(UUID id) {

    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    return Optional.ofNullable(
            jdbcTemplate.queryForObject(
                    "SELECT * FROM \"user\" WHERE id = ?",
                    AuthUserEntityRowMapper.INSTANCE,
                    id
            )
    );
  }

  @Override
  public Optional<AuthUserEntity> findByUsername(String username) {

    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    return Optional.ofNullable(
            jdbcTemplate.queryForObject(
                    "SELECT * FROM \"user\" WHERE username = ?",
                    AuthUserEntityRowMapper.INSTANCE,
                    username
            )
    );
  }
}