package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoJDBC;
import guru.qa.niffler.data.dao.impl.SpendDaoJDBC;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.tpl.DataSources;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.Connection;


public class SpendDbClient {

  private static Config CFG = Config.getInstance();
  private static final int DEFAULT_ISOLATION_LEVEL = Connection.TRANSACTION_READ_COMMITTED;

  private final CategoryDao categoryDao = new CategoryDaoJDBC();
  private final SpendDao spendDao = new SpendDaoJDBC();

  private final TransactionTemplate txTemplate = new TransactionTemplate(
          new JdbcTransactionManager(DataSources.dataSource(CFG.spendJdbcUrl()))
  );

  private final JdbcTransactionTemplate jdbcTxTemplate = new JdbcTransactionTemplate(
          CFG.spendJdbcUrl()
  );

  public SpendJson createSpend(SpendJson spend) {

    return jdbcTxTemplate.execute(() -> {
              SpendEntity spendEntity = SpendEntity.fromJson(spend);
              if (spendEntity.getCategory().getId() == null) {
                CategoryEntity categoryEntity = categoryDao.create(spendEntity.getCategory());
                spendEntity.setCategory(categoryEntity);
              }
              return SpendJson.fromEntity(spendDao.create(spendEntity));
            },
            DEFAULT_ISOLATION_LEVEL
    );
  }

  public CategoryJson createCategory(CategoryJson category) {

    return jdbcTxTemplate.execute(() -> {
              CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
              return CategoryJson.fromEntity(categoryDao.create(categoryEntity)
              );
            },
            DEFAULT_ISOLATION_LEVEL
    );
  }

  public CategoryJson updateCategory(CategoryJson category) {

    return jdbcTxTemplate.execute(() -> {

              CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
              return CategoryJson.fromEntity(categoryDao.update(categoryEntity)
              );
            },
            DEFAULT_ISOLATION_LEVEL
    );
  }

}
