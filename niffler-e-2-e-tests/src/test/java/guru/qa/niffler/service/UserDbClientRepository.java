package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.UserDao;
import guru.qa.niffler.data.dao.impl.UserDaoJDBC;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.AuthUserRepository;
import guru.qa.niffler.data.repository.impl.AuthUserRepositoryJDBC;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.AuthUserJson;

import java.util.Arrays;

public class UserDbClientRepository {

  private static Config CFG = Config.getInstance();


  private final AuthUserRepository authUserRepository = new AuthUserRepositoryJDBC();

  private final UserDao userDaoJDBC = new UserDaoJDBC();

  private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
          CFG.authJdbcUrl(),
          CFG.userdataJdbcUrl()
  );

  public AuthUserJson createUserJDBC(AuthUserJson authUserJson) {

    return xaTransactionTemplate.execute(() -> {

              AuthUserEntity authUser = new AuthUserEntity();
              authUser.setUsername(authUserJson.username());
              authUser.setPassword(authUserJson.password());
              authUser.setEnabled(authUserJson.enabled());
              authUser.setAccountNonExpired(authUserJson.accountNonExpired());
              authUser.setAccountNonLocked(authUserJson.accountNonLocked());
              authUser.setCredentialsNonExpired(authUserJson.credentialsNonExpired());
              authUser.setAuthorities(Arrays.stream(Authority.values())
                      .map(
                              e -> {
                                AuthorityEntity ae = new AuthorityEntity();
                                ae.setUser(authUser);
                                ae.setAuthority(e);
                                return ae;
                              }
                      ).toList());

              authUserRepository.createUser(authUser);

              userDaoJDBC.createUser(UserEntity.fromJson(authUserJson));

              return AuthUserJson.fromEntity(authUser);
            }
    );
  }
}