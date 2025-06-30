package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.mapper.SpendEntityRowMapper;
import guru.qa.niffler.data.tpl.DataSources;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SpendDaoSpringJDBC implements SpendDao {

  private static final Config CFG = Config.getInstance();

  @Override
  public SpendEntity create(SpendEntity spendEntity) {

    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));
    KeyHolder kh = new GeneratedKeyHolder();

    jdbcTemplate.update(
            con -> {
              PreparedStatement ps = con.prepareStatement(
                      "INSERT INTO \"spend\" (username, spend_date, currency, amount, description, category_id)" +
                              "VALUES (?,?,?,?,?,?)",
                      PreparedStatement.RETURN_GENERATED_KEYS);

              ps.setString(1, spendEntity.getUsername());
              ps.setDate(2, spendEntity.getSpendDate());
              ps.setString(3, spendEntity.getCurrency().name());
              ps.setDouble(4, spendEntity.getAmount());
              ps.setString(5, spendEntity.getDescription());
              ps.setObject(6, spendEntity.getCategory().getId());

              return ps;
            }, kh
    );

    final UUID generatedKey = (UUID) kh.getKeys().get("id");
    spendEntity.setId(generatedKey);

    return spendEntity;
  }

  @Override
  public Optional<SpendEntity> findSpendById(UUID id) {

    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));
    return Optional.ofNullable(
            jdbcTemplate.queryForObject(
                    "SELECT * FROM \"spend\" WHERE id = ?",
                    SpendEntityRowMapper.INSTANCE,
                    id
            )
    );
  }

  @Override
  public List<SpendEntity> findAllByUsername(String username) {

    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));
    return jdbcTemplate.query(
            "SELECT * FROM \"spend\" WHERE username = ?",
            SpendEntityRowMapper.INSTANCE,
            username
    );
  }

  @Override
  public List<SpendEntity> findAll() {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));
    return jdbcTemplate.query(
            "SELECT * FROM \"spend\"",
            SpendEntityRowMapper.INSTANCE
    );
  }

  @Override
  public void deleteSpend(SpendEntity spendEntity) {

    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));
    jdbcTemplate.update(
            "DELETE FROM \"spend\" WHERE id = ?",
            spendEntity.getId()
    );
  }
}