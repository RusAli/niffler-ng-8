package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthorityDao;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.tpl.DataSources;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class AuthorityDaoSpringJdbc implements AuthorityDao {

  private static final Config CFG = Config.getInstance();

  @Override
  public void createAuthority(List<AuthorityEntity> authorityEntities) {

    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
    jdbcTemplate.batchUpdate(

            "INSERT INTO \"authority\" (user_id, authority) VALUES (?,?)",
            new BatchPreparedStatementSetter() {

              @Override
              public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setObject(1, authorityEntities.get(i).getUserId());
                ps.setString(2, authorityEntities.get(i).getAuthority().name());
              }

              @Override
              public int getBatchSize() {
                return authorityEntities.size();
              }
            }
    );
  }
}