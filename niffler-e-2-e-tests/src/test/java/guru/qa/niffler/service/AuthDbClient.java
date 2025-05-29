package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.AuthUserDaoJDBC;
import guru.qa.niffler.data.dao.impl.AuthorityDaoJDBC;
import guru.qa.niffler.data.dao.impl.UserdataUserDAOJdbc;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.model.UserJson;

import java.sql.Connection;
import java.util.Arrays;
import java.util.stream.Collectors;

import static guru.qa.niffler.data.Databases.XaFunction;
import static guru.qa.niffler.data.Databases.xaTransaction;

public class AuthDbClient {

  private static Config CFG = Config.getInstance();
  private static final int DEFAULT_ISOLATION_LEVEL = Connection.TRANSACTION_READ_COMMITTED;


  public UserJson createAuthUser(UserJson userJson) {

    return xaTransaction(

            new XaFunction<>(connection -> {

              AuthUserEntity authUserEntity = new AuthUserEntity();
              authUserEntity.setUsername(userJson.username());
              authUserEntity.setPassword(userJson.password());
              authUserEntity.setEnabled(true);
              authUserEntity.setAccountNonLocked(true);
              authUserEntity.setAccountNonExpired(true);
              authUserEntity.setCredentialsNonExpired(true);
              new AuthUserDaoJDBC(connection).createUser(authUserEntity);

              new AuthorityDaoJDBC(connection).createAuthority(
                      Arrays.stream(Authority.values())
                              .map(a -> {
                                        AuthorityEntity authorityEntity = new AuthorityEntity();
                                        authorityEntity.setUserId(authUserEntity.getId());
                                        authorityEntity.setAuthority(a);
                                        return authorityEntity;
                                      }
                              ).collect(Collectors.toList()));

              return null;
            },
                    CFG.authJdbcUrl(),
                    DEFAULT_ISOLATION_LEVEL),

            new XaFunction<>(connection -> {

              UserEntity userEntity = UserEntity.fromJson(userJson);
              new UserdataUserDAOJdbc(connection).createUser(userEntity);

              return UserJson.fromEntity(userEntity);
            },
                    CFG.userdataJdbcUrl(),
                    DEFAULT_ISOLATION_LEVEL)
    );

  }
}