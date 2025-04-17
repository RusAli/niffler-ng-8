package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.NewRandomUser;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.RegisterPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
public class RegistrationTest {


  @NewRandomUser
  @Test
  void shouldRegisterNewUser(UserJson userJson) {

    Selenide.open(RegisterPage.URL, RegisterPage.class)
            .checkPageIsOpened()
            .setUsernameInput(userJson.username())
            .setPasswordInput(userJson.password())
            .setPasswordSubmitInput(userJson.password())
            .submitRegistration()
            .checkSuccessRegisterMessageIsVisible()
            .clickSignInBtn()
            .doLogin(userJson.username(), userJson.password())
            .checkThatMainPageIsShown();
  }

  @Test
  void shouldNotRegisterUseWithAnExistingUsername() {

    Selenide.open(RegisterPage.URL, RegisterPage.class)
            .checkPageIsOpened()
            .setUsernameInput("duck")
            .setPasswordInput("12345")
            .setPasswordSubmitInput("12345")
            .submitRegistration()
            .checkErrorMessageIsVisible();
  }

  @Test
  void shouldShowErrorIfPasswordAndConfirmPasswordAreNotMatch() {

    Selenide.open(RegisterPage.URL, RegisterPage.class)
            .checkPageIsOpened()
            .setUsernameInput("test1")
            .setPasswordInput("12345")
            .setPasswordSubmitInput("54321")
            .submitRegistration()
            .checkMessagePasswordAndSubmitPasswordNotEqual();
  }
}