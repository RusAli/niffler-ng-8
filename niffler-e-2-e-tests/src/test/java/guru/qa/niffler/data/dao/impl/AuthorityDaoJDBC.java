package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthorityDao;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static guru.qa.niffler.data.tpl.Connections.holder;

public class AuthorityDaoJDBC implements AuthorityDao {

  private static final Config CFG = Config.getInstance();

  @Override
  public void createAuthority(List<AuthorityEntity> authorityEntities) {
    try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
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