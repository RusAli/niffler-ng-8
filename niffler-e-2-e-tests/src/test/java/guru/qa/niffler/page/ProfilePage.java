package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class ProfilePage {

  public static final String URL = Config.getInstance().frontUrl() + "profile";

  private final SelenideElement image = $("#image__input").parent().$(".MuiAvatar-img");
  private final SelenideElement uploadNewPicture = $("#image__input");
  private final SelenideElement username = $("#username");
  private final SelenideElement name = $("#name");
  private final SelenideElement submitBtn = $("button[type='submit']");
  private final SelenideElement showArchivedCheckBox = $("input[type='checkbox']");
  private final SelenideElement categoryInput = $("#category");
  private final SelenideElement categoryContainer = $(".MuiGrid-container:has(#category)");
  private final ElementsCollection categoryElements = categoryContainer.$$(".MuiGrid-item>.MuiBox-root");

  public static final String editCategoryBtn = "button[aria-label='Edit category']";
  public static final String archiveCategoryBtn = "button[aria-label='Archive category']";
  public static final String unArchiveCategoryBtn = "button[aria-label='Unarchive category']";


  @Step("Открыть страницу ProfilePage")
  public ProfilePage openProfilePage() {
    return Selenide.open(ProfilePage.URL, ProfilePage.class);
  }

  @Step("Проверить,что страница ProfilePage открыта")
  public ProfilePage checkPageIsOpened() {
    username.should(Condition.visible);
    name.should(Condition.visible);
    submitBtn.should(Condition.visible);
    categoryContainer.should(Condition.visible);
    return this;
  }

  @Step("Найти категорию по имени")
  public SelenideElement findCategory(String categoryName) {
    return categoryElements.find(text(categoryName));
  }

  @Step("Нажать кнопку Show archived")
  public ProfilePage clickShowArchivedCheckBox() {
    showArchivedCheckBox.click();
    return this;
  }

  @Step("Проверить катеригорию в списке")
  public ProfilePage checkCategoryInList(String categoryName) {
    findCategory(categoryName).shouldBe(visible);
    return this;
  }
}