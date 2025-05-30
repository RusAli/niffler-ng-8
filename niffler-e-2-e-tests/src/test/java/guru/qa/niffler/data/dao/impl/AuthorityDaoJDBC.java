package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.data.dao.AuthorityDao;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class AuthorityDaoJDBC implements AuthorityDao {

  private final Connection connection;

  public AuthorityDaoJDBC(Connection connection) {
    this.connection = connection;
  }

  @Override
  public void createAuthority(List<AuthorityEntity> authorityEntities) {
    try (PreparedStatement ps = connection.prepareStatement(
            "INSERT INTO \"authority\" (user_id, authority) " +
                    "VALUES (?,?)",
            PreparedStatement.RETURN_GENERATED_KEYS
    )) {

      for (AuthorityEntity authority : authorityEntities) {

        ps.setObject(1, authority.getUserId());
        ps.setString(2, authority.getAuthority().name());
        ps.addBatch();
        ps.clearParameters();
      }

      ps.executeBatch();

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}