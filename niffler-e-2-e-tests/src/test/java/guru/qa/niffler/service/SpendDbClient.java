package guru.qa.niffler.service;

import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoJDBC;
import guru.qa.niffler.data.dao.impl.SpendDaoJDBC;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.SpendJson;

public class SpendDbClient {

  private final SpendDao spendDao = new SpendDaoJDBC();
  private final CategoryDao categoryDao = new CategoryDaoJDBC();

  public SpendJson createSpend(SpendJson spend) {

    SpendEntity spendEntity = SpendEntity.fromJson(spend);
    if (spendEntity.getCategory().getId() == null) {
      CategoryEntity categoryEntity = categoryDao.create(spendEntity.getCategory());
      spendEntity.setCategory(categoryEntity);
    }
    return SpendJson.fromEntity(spendDao.create(spendEntity));
  }
}
