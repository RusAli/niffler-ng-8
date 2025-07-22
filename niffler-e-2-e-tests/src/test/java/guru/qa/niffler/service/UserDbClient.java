package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.dao.AuthorityDao;
import guru.qa.niffler.data.dao.UserDao;
import guru.qa.niffler.data.dao.impl.*;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.UserJson;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Arrays;
import java.util.List;

import static guru.qa.niffler.data.tpl.DataSources.dataSource;

public class UserDbClient {

  private static Config CFG = Config.getInstance();

  // Spring
  private final AuthUserDao authUserDaoSpring = new AuthUserDaoSpringJdbc();
  private final AuthorityDao authorityDaoSpring = new AuthorityDaoSpringJdbc();
  private final UserDao userDaoSpring = new UserDaoSpringJdbc();

  // JDBC
  private final AuthUserDao authUserDao = new AuthUserDaoJDBC();
  private final AuthorityDao authorityDao = new AuthorityDaoJDBC();
  private final UserDao userDao = new UserDaoJDBC();

  private final TransactionTemplate txTemplate = new TransactionTemplate(
          new JdbcTransactionManager(dataSource(CFG.authJdbcUrl()))
  );

  private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
          CFG.authJdbcUrl(),
          CFG.userdataJdbcUrl()
  );

  private final TransactionTemplate chainedTxTemplate = new TransactionTemplate(
          new ChainedTransactionManager(
                  new JdbcTransactionManager(dataSource(CFG.authJdbcUrl())),
                  new JdbcTransactionManager(dataSource(CFG.userdataJdbcUrl()))
          )
  );

  public UserJson createUserSpringJdbc(UserJson userJson) {

    return txTemplate.execute(conn -> {

              AuthUserEntity authUser = new AuthUserEntity();
              authUser.setUsername(userJson.username());
              authUser.setEnabled(true);
              authUser.setAccountNonExpired(true);
              authUser.setAccountNonLocked(true);
              authUser.setCredentialsNonExpired(true);

              AuthUserEntity createdAuthUser = authUserDaoSpring.createUser(authUser);

              List<AuthorityEntity> userAuthorities = Arrays.stream(Authority.values())
                      .map(
                              e -> {

                                AuthorityEntity ae = new AuthorityEntity();
                                ae.setUser(createdAuthUser);
                                ae.setAuthority(e);
                                return ae;
                              }
                      ).toList();

              authorityDaoSpring.createAuthority(userAuthorities);

              return UserJson.fromEntity(
                      userDaoSpring.createUser(UserEntity.fromJson(userJson))
              );
            }
    );
  }

  public UserJson xaCreateUserSpringJdbc(UserJson userJson) {

    return xaTransactionTemplate.execute(() -> {

              AuthUserEntity authUser = new AuthUserEntity();
              authUser.setUsername(userJson.username());
              authUser.setEnabled(true);
              authUser.setAccountNonExpired(true);
              authUser.setAccountNonLocked(true);
              authUser.setCredentialsNonExpired(true);

              AuthUserEntity createdAuthUser = authUserDaoSpring.createUser(authUser);

              List<AuthorityEntity> userAuthorities = Arrays.stream(Authority.values())
                      .map(
                              e -> {

                                AuthorityEntity ae = new AuthorityEntity();
                                ae.setUser(createdAuthUser);
                                ae.setAuthority(e);
                                return ae;
                              }
                      ).toList();

              authorityDaoSpring.createAuthority(userAuthorities);

              return UserJson.fromEntity(
                      userDaoSpring.createUser(UserEntity.fromJson(userJson))
              );
            }
    );
  }

  public UserJson createUserJdbc(UserJson userJson) {

    return txTemplate.execute(conn -> {

              AuthUserEntity authUser = new AuthUserEntity();
              authUser.setUsername(userJson.username());
              authUser.setEnabled(true);
              authUser.setAccountNonExpired(true);
              authUser.setAccountNonLocked(true);
              authUser.setCredentialsNonExpired(true);

              AuthUserEntity createdAuthUser = authUserDao.createUser(authUser);

              List<AuthorityEntity> userAuthorities = Arrays.stream(Authority.values())
                      .map(
                              e -> {

                                AuthorityEntity ae = new AuthorityEntity();
                                ae.setUser(createdAuthUser);
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

  public UserJson xaCreateUserJdbc(UserJson userJson) {

    return xaTransactionTemplate.execute(() -> {

              AuthUserEntity authUser = new AuthUserEntity();
              authUser.setUsername(userJson.username());
              authUser.setEnabled(true);
              authUser.setAccountNonExpired(true);
              authUser.setAccountNonLocked(true);
              authUser.setCredentialsNonExpired(true);

              AuthUserEntity createdAuthUser = authUserDao.createUser(authUser);

              List<AuthorityEntity> userAuthorities = Arrays.stream(Authority.values())
                      .map(
                              e -> {

                                AuthorityEntity ae = new AuthorityEntity();
                                ae.setUser(createdAuthUser);
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

  public UserJson chainedCreateUserSpringJdbc(UserJson userJson) {

    return chainedTxTemplate.execute(conn -> {

              AuthUserEntity authUser = new AuthUserEntity();
              authUser.setUsername(userJson.username());
              authUser.setEnabled(true);
              authUser.setAccountNonExpired(true);
              authUser.setAccountNonLocked(true);
              authUser.setCredentialsNonExpired(true);

              AuthUserEntity createdAuthUser = authUserDaoSpring.createUser(authUser);

              List<AuthorityEntity> userAuthorities = Arrays.stream(Authority.values())
                      .map(
                              e -> {

                                AuthorityEntity ae = new AuthorityEntity();
                                ae.setUser(createdAuthUser);
                                ae.setAuthority(e);
                                return ae;
                              }
                      ).toList();

              authorityDaoSpring.createAuthority(userAuthorities);


              return UserJson.fromEntity(
                      userDaoSpring.createUser(UserEntity.fromJson(userJson))
              );
            }
    );
  }

  public UserJson chainedCreateUserSpringJdbcWithError(UserJson userJson) {

    return chainedTxTemplate.execute(conn -> {

              AuthUserEntity authUser = new AuthUserEntity();
              authUser.setUsername(userJson.username());
              authUser.setEnabled(true);
              authUser.setAccountNonExpired(true);
              authUser.setAccountNonLocked(true);
              authUser.setCredentialsNonExpired(true);

              AuthUserEntity createdAuthUser = authUserDaoSpring.createUser(authUser);

              List<AuthorityEntity> userAuthorities = Arrays.stream(Authority.values())
                      .map(
                              e -> {

                                AuthorityEntity ae = new AuthorityEntity();
                                ae.setUser(createdAuthUser);
                                ae.setAuthority(e);
                                return ae;
                              }
                      ).toList();

              authorityDaoSpring.createAuthority(userAuthorities);

              return UserJson.fromEntity(userDaoSpring.createUser(UserEntity.fromJson(userJson)));
            }
    );
  }
}