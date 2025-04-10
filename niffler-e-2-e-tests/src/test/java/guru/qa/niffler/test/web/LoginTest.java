package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.BrowserExtension;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
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