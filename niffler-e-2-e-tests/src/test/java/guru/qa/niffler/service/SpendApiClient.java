package guru.qa.niffler.service;

import guru.qa.niffler.api.SpendApi;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;

import static org.apache.hc.core5.http.HttpStatus.SC_CREATED;
import static org.apache.hc.core5.http.HttpStatus.SC_OK;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpendApiClient implements SpendClient {

  private static final Config CFG = Config.getInstance();

  private final OkHttpClient client = new OkHttpClient.Builder().build();
  private final Retrofit retrofit = new Retrofit.Builder()
          .baseUrl(CFG.spendUrl())
          .client(client)
          .addConverterFactory(JacksonConverterFactory.create())
          .build();

  private final SpendApi spendApi = retrofit.create(SpendApi.class);

  @Override
  public SpendJson createSpend(SpendJson spend) {

    final Response<SpendJson> response;
    try {
      response = spendApi.addSpend(spend)
              .execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(SC_CREATED, response.code());
    return response.body();
  }

  @Override
  public CategoryJson createCategory(CategoryJson category) {

    final Response<CategoryJson> response;
    try {
      response = spendApi.addCategory(category)
              .execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(SC_OK, response.code());
    return response.body();
  }

  @Override
  public void deleteCategory(CategoryJson category) {
    throw new RuntimeException("Delete category is not allowed");
  }
}
