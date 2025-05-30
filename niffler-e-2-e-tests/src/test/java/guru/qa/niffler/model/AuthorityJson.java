package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;

import java.util.UUID;

public record AuthorityJson(

        @JsonProperty("id")
        UUID id,
        @JsonProperty("authority")
        Authority authority,
        @JsonProperty("userId")
        UUID userId) {

  public static AuthorityJson fromEntity(AuthorityEntity authorityEntity) {
    return new AuthorityJson(
            authorityEntity.getId(),
            authorityEntity.getAuthority(),
            authorityEntity.getUserId());
  }
}