package guru.qa.niffler.data.mapper;

import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.model.CurrencyValues;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UserEntityRowMapper implements RowMapper<UserEntity> {

  public static final UserEntityRowMapper INSTANCE = new UserEntityRowMapper();

  private UserEntityRowMapper() {
  }

  @Override
  public UserEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
    UserEntity userEntity = new UserEntity();

    userEntity.setId(rs.getObject("id", UUID.class));
    userEntity.setUsername(rs.getString("username"));
    userEntity.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
    userEntity.setFirstname(rs.getString("firstname"));
    userEntity.setSurname(rs.getString("surname"));
    userEntity.setPhoto(rs.getBytes("photo"));
    userEntity.setPhotoSmall(rs.getBytes("photoSmall"));
    userEntity.setFullname(rs.getString("full_name"));

    return userEntity;
  }
}