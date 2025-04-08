package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;


public record UserJson(

        @JsonProperty("id")
        UUID id,
        @JsonProperty("username")
        String username,
        @JsonProperty("password")
        String password,
        @JsonProperty("fullname")
        String fullname,
        @JsonProperty("photo")
        String photo,
        @JsonProperty("__typename")
        String typename) {
}