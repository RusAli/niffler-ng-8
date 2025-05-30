package guru.qa.niffler.data.entity.auth;

import guru.qa.niffler.model.AuthorityJson;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
public class AuthorityEntity implements Serializable {

  private UUID id;

  private Authority authority;

  private UUID userId;

  public static AuthorityEntity fromJson(AuthorityJson authorityJson) {
    AuthorityEntity entity = new AuthorityEntity();
    entity.setId(authorityJson.id());
    entity.setAuthority(authorityJson.authority());
    entity.setUserId(authorityJson.userId());

    return entity;
  }
}