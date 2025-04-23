package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

@WebTest
public class LoginTest {

  @Test
  void mainPageShouldBeDisplayedAfterSuccessfulLogin() {
    Selenide.open(LoginPage.URL, LoginPage.class)
            .doLogin("duck", "12345")
            .checkThatMainPageIsShown();
  }

  @Test
  void userShouldStayOnLoginPageAfterUnsuccessfulLogin() {
    Selenide.open(LoginPage.URL, LoginPage.class)
            .doLoginWithWrongCred("duck", "123")
            .checkThatLoginPageIsShown()
            .checkMessageWithWrongCredentials();
  }
}