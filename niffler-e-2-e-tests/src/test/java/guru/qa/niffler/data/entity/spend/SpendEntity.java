package guru.qa.niffler.data.entity.spend;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Date;
import java.util.UUID;

@Getter
@Setter
public class SpendEntity implements Serializable {

  private UUID id;

  private String username;

  private CurrencyValues currency;

  private Date spendDate;

  private Double amount;

  private String description;

  private CategoryEntity category;


  public static SpendEntity fromJson(SpendJson spendJson) {

    SpendEntity spendEntity = new SpendEntity();
    spendEntity.setId(spendJson.id());
    spendEntity.setUsername(spendJson.username());
    spendEntity.setCurrency(spendJson.currency());
    spendEntity.setSpendDate(new java.sql.Date(spendJson.spendDate().getTime()));
    spendEntity.setAmount(spendJson.amount());
    spendEntity.setDescription(spendJson.description());
    spendEntity.setCategory(CategoryEntity.fromJson(spendJson.category()));

    return spendEntity;
  }
}