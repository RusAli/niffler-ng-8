package guru.qa.niffler.data.entity.auth;

import guru.qa.niffler.model.AuthUserJson;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
public class AuthUserEntity implements Serializable {

  private UUID id;

  private String username;

  private String password;

  private Boolean enabled;

  private Boolean accountNonExpired;

  private Boolean accountNonLocked;

  private Boolean credentialsNonExpired;

  public static AuthUserEntity fromJson(AuthUserJson authUserJson) {
    AuthUserEntity entity = new AuthUserEntity();
    entity.setId(authUserJson.id());
    entity.setUsername(authUserJson.username());
    entity.setPassword(authUserJson.password());
    entity.setEnabled(authUserJson.enabled());
    entity.setAccountNonExpired(authUserJson.accountNonExpired());
    entity.setAccountNonLocked(authUserJson.accountNonLocked());
    entity.setCredentialsNonExpired(authUserJson.credentialsNonExpired());
    return entity;
  }
}