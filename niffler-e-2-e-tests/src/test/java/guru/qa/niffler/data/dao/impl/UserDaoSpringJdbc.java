package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.UserDao;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.mapper.UserEntityRowMapper;
import guru.qa.niffler.data.tpl.DataSources;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.util.Optional;
import java.util.UUID;

public class UserDaoSpringJdbc implements UserDao {

  private static final Config CFG = Config.getInstance();

  @Override
  public UserEntity createUser(UserEntity userEntity) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.userdataJdbcUrl()));
    KeyHolder kh = new GeneratedKeyHolder();

    jdbcTemplate.update(
            con -> {

              PreparedStatement ps = con.prepareStatement(
                      "INSERT INTO \"user\" (username, currency, firstname, surname, photo, photo_small, full_name) " +
                              "VALUES (?,?,?,?,?,?,?)",
                      PreparedStatement.RETURN_GENERATED_KEYS);

              ps.setString(1, userEntity.getUsername());
              ps.setString(2, userEntity.getCurrency().name());
              ps.setString(3, userEntity.getFirstname());
              ps.setString(4, userEntity.getSurname());
              ps.setBytes(5, userEntity.getPhoto());
              ps.setBytes(6, userEntity.getPhotoSmall());
              ps.setString(7, userEntity.getFullname());
              return ps;
            }, kh
    );

    final UUID generatedKey = (UUID) kh.getKeys().get("id");
    userEntity.setId(generatedKey);

    return userEntity;
  }

  @Override
  public Optional<UserEntity> findById(UUID id) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.userdataJdbcUrl()));
    return Optional.ofNullable(
            jdbcTemplate.queryForObject(
                    "SELECT * FROM \"user\" WHERE id = ?",
                    UserEntityRowMapper.INSTANCE,
                    id
            )
    );
  }

  @Override
  public Optional<UserEntity> findByUsername(String username) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.userdataJdbcUrl()));
    return Optional.ofNullable(
            jdbcTemplate.queryForObject(
                    "SELECT * FROM \"user\" WHERE username = ?",
                    UserEntityRowMapper.INSTANCE,
                    username
            )
    );
  }

  @Override
  public void delete(UserEntity userEntity) {

  }
}