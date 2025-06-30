package guru.qa.niffler.test;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UserDbClient;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.Test;

public class JdbcTest {


//  @Test
//  void jdbcTest() {
//
//    SpendDbClient spendDbClient = new SpendDbClient();
//
//    SpendJson spend = spendDbClient.createSpend(
//            new SpendJson(
//                    null,
//                    new Date(),
//                    new CategoryJson(
//                            null,
//                            RandomDataUtils.randomCategory(),
//                            "duck",
//                            false
//                    ),
//                    CurrencyValues.RUB,
//                    100.0,
//                    RandomDataUtils.randomSentence(5),
//                    "duck"
//            )
//    );
//    System.out.println(spend);
//  }

//  @Test
//  void xaTransactionsTest() {
//    AuthDbClient authDbClient = new AuthDbClient();
//    String username = RandomDataUtils.randomUsername();
//
//    UserJson userJson = authDbClient.createAuthUser(
//            new UserJson(
//                    null,
//                    "password",
//                    username,
//                    null,
//                    null,
//                    null,
//                    CurrencyValues.EUR,
//                    null,
//                    null
//            )
//    );
//  }

  @Test
  void jdbcSpringTest() {

    UserDbClient userDbClient = new UserDbClient();
    String username = RandomDataUtils.randomUsername();

    UserJson userJson = userDbClient.createUserSpringJdbc(
            new UserJson(
                    null,
                    null,
                    "duck8",
                    null,
                    null,
                    null,
                    CurrencyValues.EUR,
                    null,
                    null
            )
    );

    System.out.println(userJson);

  }
}