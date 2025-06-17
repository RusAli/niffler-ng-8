package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.data.dao.AuthorityDao;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class AuthorityDaoSpringJdbc implements AuthorityDao {

  private final DataSource dataSource;

  public AuthorityDaoSpringJdbc(DataSource dataSource) {
    this.dataSource = dataSource;
  }


  @Override
  public void createAuthority(List<AuthorityEntity> authorityEntities) {

    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
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