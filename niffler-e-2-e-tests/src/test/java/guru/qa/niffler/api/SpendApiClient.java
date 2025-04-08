package guru.qa.niffler.api;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.PeriodValues;
import guru.qa.niffler.model.SpendJson;
import okhttp3.OkHttpClient;
import org.eclipse.jetty.http.HttpStatus;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpendApiClient {

  private static final Config CFG = Config.getInstance();

  private final OkHttpClient client = new OkHttpClient.Builder().build();
  private final Retrofit retrofit = new Retrofit.Builder()
          .baseUrl(CFG.spendUrl())
          .client(client)
          .addConverterFactory(JacksonConverterFactory.create())
          .build();

  private final SpendApi spendApi = retrofit.create(SpendApi.class);


  public SpendJson addSpend(SpendJson spend) {
    Response<SpendJson> response;
    try {
      response = spendApi.addSpend(spend).execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(HttpStatus.CREATED_201, response.code());
    return response.body();
  }

  public SpendJson editSpend(SpendJson spend) {
    Response<SpendJson> response;
    try {
      response = spendApi.editSpend(spend).execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(HttpStatus.OK_200, response.code());
    return response.body();
  }

  public SpendJson getSpendById(String id) {
    Response<SpendJson> response;
    try {
      response = spendApi.getSpendById(id).execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(HttpStatus.OK_200, response.code());
    return response.body();
  }

  public List<SpendJson> getSpendAll(CurrencyValues filterCurrency, PeriodValues filterPeriod) {
    Response<List<SpendJson>> response;
    try {
      response = spendApi.getSpendAll(filterCurrency, filterPeriod).execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(HttpStatus.OK_200, response.code());
    return response.body();
  }

  public void removeSpendsById(List<String> ids) {
    Response<Void> response;
    try {
      response = spendApi.removeSpendsById(ids).execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(HttpStatus.OK_200, response.code());
  }

  public CategoryJson addCategory(CategoryJson categoryJson) {
    Response<CategoryJson> response;
    try {
      response = spendApi.addCategory(categoryJson).execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(HttpStatus.OK_200, response.code());
    return response.body();
  }

  public CategoryJson updateCategory(CategoryJson categoryJson) {
    Response<CategoryJson> response;
    try {
      response = spendApi.updateCategory(categoryJson).execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(HttpStatus.OK_200, response.code());
    return response.body();
  }

  public List<CategoryJson> getAllCategories(Boolean excludeArchived) {
    Response<List<CategoryJson>> response;
    try {
      response = spendApi.getAllCategories(excludeArchived).execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(HttpStatus.OK_200, response.code());
    return response.body();
  }
}
