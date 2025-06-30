package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.dao.AuthorityDao;
import guru.qa.niffler.data.dao.UserDao;
import guru.qa.niffler.data.dao.impl.AuthUserDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.AuthorityDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.UserDaoSpringJdbc;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.tpl.DataSources;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.UserJson;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Arrays;
import java.util.List;

public class UserDbClient {

  private static Config CFG = Config.getInstance();

  private final AuthUserDao authUserDao = new AuthUserDaoSpringJdbc();
  private final AuthorityDao authorityDao = new AuthorityDaoSpringJdbc();
  private final UserDao userDao = new UserDaoSpringJdbc();

  private final TransactionTemplate txTemplate = new TransactionTemplate(
          new JdbcTransactionManager(DataSources.dataSource(CFG.authJdbcUrl()))
  );

  private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
          CFG.authJdbcUrl(),
          CFG.userdataJdbcUrl()
  );


  public UserJson createUserSpringJdbc(UserJson userJson) {

    return xaTransactionTemplate.execute(() -> {

              AuthUserEntity authUser = new AuthUserEntity();
              authUser.setUsername(userJson.username());
              authUser.setPassword(userJson.password());
              authUser.setEnabled(true);
              authUser.setAccountNonExpired(true);
              authUser.setAccountNonLocked(true);
              authUser.setCredentialsNonExpired(true);

              AuthUserEntity createdAuthUser = authUserDao.createUser(authUser);

              List<AuthorityEntity> userAuthorities = Arrays.stream(Authority.values())
                      .map(
                              e -> {

                                AuthorityEntity ae = new AuthorityEntity();
                                ae.setUserId(createdAuthUser.getId());
                                ae.setAuthority(e);
                                return ae;
                              }
                      ).toList();

              authorityDao.createAuthority(userAuthorities);

              return UserJson.fromEntity(
                      userDao.createUser(UserEntity.fromJson(userJson))
              );
            }
    );
  }
}