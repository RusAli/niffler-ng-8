package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.CategoryDaoJDBC;
import guru.qa.niffler.data.dao.impl.SpendDaoJDBC;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import static guru.qa.niffler.data.Databases.transaction;

public class SpendDbClient {

  private static Config CFG = Config.getInstance();


  public SpendJson createSpend(SpendJson spend) {

    return transaction(connection -> {
              SpendEntity spendEntity = SpendEntity.fromJson(spend);
              if (spendEntity.getCategory().getId() == null) {
                CategoryEntity categoryEntity = new CategoryDaoJDBC(connection)
                        .create(spendEntity.getCategory());
                spendEntity.setCategory(categoryEntity);
              }
              return SpendJson.fromEntity(
                      new SpendDaoJDBC(connection)
                              .create(spendEntity));
            },
            CFG.spendJdbcUrl()
    );
  }

  public CategoryJson createCategory(CategoryJson category) {

    return transaction(connection -> {
              CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
              return CategoryJson.fromEntity(
                      new CategoryDaoJDBC(connection).create(categoryEntity)
              );
            },
            CFG.spendJdbcUrl()
    );
  }

  public CategoryJson updateCategory(CategoryJson category) {

    return transaction(connection -> {

              CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
              return CategoryJson.fromEntity(
                      new CategoryDaoJDBC(connection).update(categoryEntity)
              );
            },
            CFG.spendJdbcUrl()
    );
  }

}
