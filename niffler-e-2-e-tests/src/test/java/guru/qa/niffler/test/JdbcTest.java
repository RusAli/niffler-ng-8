package guru.qa.niffler.test;

import guru.qa.niffler.model.*;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.service.UserDbClient;
import guru.qa.niffler.service.UserDbClientRepository;
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


  @Test
  void createUserJdbcTest() {

    UserDbClient userDbClient = new UserDbClient();
    String username = RandomDataUtils.randomUsername();

    UserJson userJson = userDbClient.createUserJdbc(
            new UserJson(
                    null,
                    username,
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

  @Test
  void xaCreateUserJdbcTest() {

    UserDbClient userDbClient = new UserDbClient();
    String username = RandomDataUtils.randomUsername();

    UserJson userJson = userDbClient.xaCreateUserJdbc(
            new UserJson(
                    null,
                    username,
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

  @Test
  void createUserSpringJdbcTest() {

    UserDbClient userDbClient = new UserDbClient();
    String username = RandomDataUtils.randomUsername();

    UserJson userJson = userDbClient.createUserSpringJdbc(
            new UserJson(
                    null,
                    "123456",
                    username,
                    null,
                    null,
                    CurrencyValues.EUR,
                    null,
                    null
            )
    );
    System.out.println(userJson);
  }

  @Test
  void xaCreateUserSpringJdbcTest() {

    UserDbClient userDbClient = new UserDbClient();
    String username = RandomDataUtils.randomUsername();

    UserJson userJson = userDbClient.xaCreateUserSpringJdbc(
            new UserJson(
                    null,
                    null,
                    username,
                    null,
                    null,
                    CurrencyValues.EUR,
                    null,
                    null
            )
    );
    System.out.println(userJson);
  }

  @Test
  void chainedTxManagerCreateUserTest() {

    UserDbClient userDbClient = new UserDbClient();
    String username = RandomDataUtils.randomUsername();

    UserJson userJson = userDbClient.chainedCreateUserSpringJdbc(
            new UserJson(
                    null,
                    "123456",
                    username,
                    null,
                    null,
                    CurrencyValues.EUR,
                    null,
                    null
            )
    );
  }


  /*
  Использование двух менеджеров транзакций на основе AbstractPlatformTransactionManager приводит к тому,
  что первый менеджер транзакций обрабатывает все синхронизации независимо от источника ресурсов.
  Если второй менеджер транзакций не может выполнить фиксацию,
  то все синхронизации уже обработаны и восстановить их невозможно.
   */
  @Test
  void chainedTxManagerCreateUserTestWithError() {

    UserDbClient userDbClient = new UserDbClient();
    String username = RandomDataUtils.randomUsername();

    UserJson userJson = userDbClient.chainedCreateUserSpringJdbcWithError(
            new UserJson(
                    null,
                    null,
                    username,
                    null,
                    null,
                    CurrencyValues.EUR,
                    null,
                    null
            )
    );
  }


  @Test
  void createUserFromUserDbClientRepository() {

    UserDbClientRepository userDbClient = new UserDbClientRepository();
    String username = RandomDataUtils.randomUsername();

    AuthUserJson userJson = userDbClient.createUserJDBC(
            new AuthUserJson(
                    null,
                    username,
                    "12345",
                    true,
                    true,
                    true,
                    true
            )
    );
    System.out.println(userJson);
  }
}