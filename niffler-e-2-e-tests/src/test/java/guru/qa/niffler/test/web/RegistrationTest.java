package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.NewRandomUser;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.AuthUserJson;
import guru.qa.niffler.page.RegisterPage;
import org.junit.jupiter.api.Test;

@WebTest
public class RegistrationTest {


  @NewRandomUser
  @Test
  void shouldRegisterNewUser(AuthUserJson authUserJson) {

    Selenide.open(RegisterPage.URL, RegisterPage.class)
            .checkPageIsOpened()
            .setUsernameInput(authUserJson.username())
            .setPasswordInput(authUserJson.password())
            .setPasswordSubmitInput(authUserJson.password())
            .submitRegistration()
            .checkSuccessRegisterMessageIsVisible()
            .clickSignInBtn()
            .doLogin(authUserJson.username(), authUserJson.password())
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