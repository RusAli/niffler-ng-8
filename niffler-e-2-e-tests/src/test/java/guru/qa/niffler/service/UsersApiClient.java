package guru.qa.niffler.service;

import guru.qa.niffler.api.UserApi;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.UserJson;
import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;

import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;
import static org.apache.hc.core5.http.HttpStatus.SC_OK;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UsersApiClient implements UsersClient {

  private static final Config CFG = Config.getInstance();
  private static final String DEFAULT_PASS = "12345";

  private final OkHttpClient client = new OkHttpClient.Builder().build();

  private final Retrofit retrofit = new Retrofit.Builder()
          .baseUrl(CFG.userdataUrl())
          .client(client)
          .addConverterFactory(JacksonConverterFactory.create())
          .build();

  private final UserApi userApi = retrofit.create(UserApi.class);

  @Override
  public UserJson createUser(String username, String password) {
    return new UsersDbClient().createUser(username, password);
  }

  @Override
  public void addIncomeInvitation(UserJson targetUser, int count) {

    if (count > 0) {
      for (int i = 0; i < count; i++) {
        String username = randomUsername();
        final Response<UserJson> response;
        createUser(username, DEFAULT_PASS);
        try {
          response = userApi.sendInvitation(username, targetUser.username())
                  .execute();
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
        assertEquals(SC_OK, response.code());
      }
    }
  }

  @Override
  public void addOutcomeInvitation(UserJson targetUser, int count) {

    if (count > 0) {
      for (int i = 0; i < count; i++) {
        String username = randomUsername();
        final Response<UserJson> response;
        createUser(username, DEFAULT_PASS);
        try {
          response = userApi.sendInvitation(targetUser.username(), username)
                  .execute();
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
        assertEquals(SC_OK, response.code());
      }
    }
  }

  @Override
  public void addFriend(UserJson targetUser, int count) {
    if (count > 0) {
      for (int i = 0; i < count; i++) {
        String username = randomUsername();
        final Response<UserJson> invitationResponse;
        final Response<UserJson> acceptResponse;
        createUser(username, DEFAULT_PASS);
        try {
          invitationResponse = userApi.sendInvitation(username, targetUser.username()).execute();
          acceptResponse = userApi.acceptInvitation(targetUser.username(), username).execute();

        } catch (IOException e) {
          throw new RuntimeException(e);
        }
        assertEquals(SC_OK, invitationResponse.code());
        assertEquals(SC_OK, acceptResponse.code());
      }
    }
  }
}

