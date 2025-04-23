package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.ProfilePage;
import org.junit.jupiter.api.Test;

@WebTest
public class ProfileTest {

  @Category(
          username = "duck",
          archived = true
  )
  @Test
  void archivedCategoryShouldPresentInCategoriesList(CategoryJson category) {
    Selenide.open(LoginPage.URL, LoginPage.class)
            .doLogin("duck", "12345")
            .checkThatMainPageIsShown();

    Selenide.open(ProfilePage.URL, ProfilePage.class)
            .checkPageIsOpened()
            .clickShowArchivedCheckBox()
            .checkCategoryInList(category.name());
  }

  @Category(username = "duck")
  @Test
  void activeCategoryShouldPresentInCategoriesList(CategoryJson category) {
    Selenide.open(LoginPage.URL, LoginPage.class)
            .doLogin("duck", "12345")
            .checkThatMainPageIsShown();

    Selenide.open(ProfilePage.URL, ProfilePage.class)
            .checkPageIsOpened()
            .checkCategoryInList(category.name());
  }
}