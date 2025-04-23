package guru.qa.niffler.utils;

import com.github.javafaker.Faker;

public class RandomDataUtils {

  private static final Faker faker = new Faker();

  public static String randomUsername() {
    return faker.name().firstName();
  }

  public static String randomName() {
    return faker.name().name();
  }

  public static String randomSurname() {
    return faker.name().lastName();
  }

  public static String randomPassword() {
    return faker.internet().password(3, 12);
  }

  public static String randomCategory() {
    return faker.commerce().productName();
  }

  public static String randomSentence(int wordsCount) {
    return faker.lorem().sentence(wordsCount);
  }
}