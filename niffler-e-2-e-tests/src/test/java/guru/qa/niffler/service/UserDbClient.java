package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.AuthUserDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.AuthorityDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.UserDaoSpringJdbc;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.model.UserJson;

import java.util.Arrays;
import java.util.List;

import static guru.qa.niffler.data.Databases.dataSource;

public class UserDbClient {

  private static Config CFG = Config.getInstance();

  public UserJson createUserSpringJdbc(UserJson userJson) {

    AuthUserEntity authUser = new AuthUserEntity();
    authUser.setUsername(userJson.username());
    authUser.setPassword(userJson.password());
    authUser.setEnabled(true);
    authUser.setAccountNonExpired(true);
    authUser.setAccountNonLocked(true);
    authUser.setCredentialsNonExpired(true);

    AuthUserEntity createdAuthUser = new AuthUserDaoSpringJdbc(dataSource(CFG.authJdbcUrl()))
            .createUser(authUser);

    List<AuthorityEntity> userAuthorities = Arrays.stream(Authority.values())
            .map(
                    e -> {

                      AuthorityEntity ae = new AuthorityEntity();
                      ae.setUserId(createdAuthUser.getId());
                      ae.setAuthority(e);

                      return ae;
                    }

            ).toList();

    new AuthorityDaoSpringJdbc(dataSource(CFG.authJdbcUrl()))
            .createAuthority(userAuthorities);

    return UserJson.fromEntity(
            new UserDaoSpringJdbc(dataSource(CFG.userdataJdbcUrl()))
                    .createUser(
                            UserEntity.fromJson(userJson)
                    )
    );
  }
}