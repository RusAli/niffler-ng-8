package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.extractor.UserdataEntityResultSetExtractor;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import guru.qa.niffler.data.tpl.DataSources;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.util.Optional;
import java.util.UUID;

public class UserdataUserRepositorySpringJdbc implements UserdataUserRepository {

  private static final Config CFG = Config.getInstance();

  @Override
  public UserEntity create(UserEntity userEntity) {

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
            jdbcTemplate.query(
                    "SELECT u.*, f.requester_id, f.addressee_id, f.status, f.created_date " +
                            "FROM \"user\" u " +
                            "LEFT JOIN friendship f ON u.id = f.requester_id OR u.id = f.addressee_id " +
                            "WHERE u.id = ?",
                    UserdataEntityResultSetExtractor.INSTANCE,
                    id
            )
    );
  }

  @Override
  public void addIncomeInvitation(UserEntity requester, UserEntity addressee) {

    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.userdataJdbcUrl()));
    jdbcTemplate.update(
            "INSERT INTO friendship (requester_id, addressee_id, status, created_date)" +
                    "VALUES (?, ?, ?, ?)",
            requester.getId(),
            addressee.getId(),
            FriendshipStatus.PENDING.name(),
            new java.sql.Date(System.currentTimeMillis())
    );
  }

  @Override
  public void addOutcomeInvitation(UserEntity requester, UserEntity addressee) {

    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.userdataJdbcUrl()));
    jdbcTemplate.update(
            "INSERT INTO friendship (requester_id, addressee_id, status, created_date)" +
                    "VALUES (?, ?, ?, ?)",
            requester.getId(),
            addressee.getId(),
            FriendshipStatus.PENDING.name(),
            new java.sql.Date(System.currentTimeMillis())
    );
  }

  @Override
  public void addFriend(UserEntity requester, UserEntity addressee) {

    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.userdataJdbcUrl()));
    jdbcTemplate.update(
            "INSERT INTO \"friendship\" (requester_id, addressee_id, status, created_date)" +
                    " VALUES (?, ?, ?, ?)",
            requester.getId(),
            addressee.getId(),
            FriendshipStatus.ACCEPTED.name(),
            new java.sql.Date(System.currentTimeMillis())
    );
  }
}