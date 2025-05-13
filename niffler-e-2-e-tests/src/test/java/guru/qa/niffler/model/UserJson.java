package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.data.entity.userdata.UserEntity;

import java.util.UUID;


public record UserJson(

        @JsonProperty("id")
        UUID id,
        @JsonProperty("password")
        String password,
        @JsonProperty("username")
        String username,
        @JsonProperty("firstname")
        String firstname,
        @JsonProperty("surname")
        String surname,
        @JsonProperty("fullname")
        String fullname,
        @JsonProperty("currency")
        CurrencyValues currency,
        @JsonProperty("photo")
        byte[] photo,
        @JsonProperty("photoSmall")
        byte[] photoSmall) {

  public static UserJson fromEntity(UserEntity userEntity) {
    return new UserJson(
            userEntity.getId(),
            userEntity.getPassword(),
            userEntity.getUsername(),
            userEntity.getFirstname(),
            userEntity.getSurname(),
            userEntity.getFullname(),
            userEntity.getCurrency(),
            userEntity.getPhoto(),
            userEntity.getPhotoSmall()
    );
  }
}