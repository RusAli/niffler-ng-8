package guru.qa.niffler.test;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class JdbcTest {


  @Test
  void jdbcTest() {

    SpendDbClient spendDbClient = new SpendDbClient();

    SpendJson spend = spendDbClient.createSpend(
            new SpendJson(
                    null,
                    new Date(),
                    new CategoryJson(
                            null,
                            RandomDataUtils.randomCategory(),
                            "duck",
                            false
                    ),
                    CurrencyValues.RUB,
                    100.0,
                    RandomDataUtils.randomSentence(5),
                    "duck"
            )
    );
    System.out.println(spend);
  }
}