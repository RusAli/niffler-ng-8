package guru.qa.niffler.page;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class RegisterPage {

  public static final String URL = Config.getInstance().authUrl() + "register";


  private final SelenideElement usernameInput = $("#username");
  private final SelenideElement passwordInput = $("#password");
  private final SelenideElement passwordSubmitInput = $("#passwordSubmit");
  private final SelenideElement passwordBtn = $("#passwordBtn");
  private final SelenideElement passwordSubmitBtn = $("#passwordSubmitBtn");
  private final SelenideElement signUpBtn = $("button[type='submit']");
  private final SelenideElement successMessageElement = $(".form__paragraph_success");
  private final SelenideElement errorMessageElement = $(".form__error");
  private final SelenideElement signInBtn = $(".form_sign-in");
  private final SelenideElement logInLink = $(".form__link");
  private final SelenideElement passwordEqualMessage = $(byText("Passwords should be equal"));

  @Step("Открыть страницу RegisterPage")
  public RegisterPage openRegisterPage() {
    return Selenide.open(RegisterPage.URL, RegisterPage.class);
  }

  @Step("Проверить,что страница RegisterPage открыта")
  public RegisterPage checkPageIsOpened() {
    usernameInput.should(visible);
    passwordInput.should(visible);
    passwordSubmitInput.should(visible);
    return this;
  }

  @Step("Ввести в поле Username: {0}")
  public RegisterPage setUsernameInput(String username) {
    usernameInput.setValue(username);
    return this;
  }

  @Step("Ввести в поле Password: {0}")
  public RegisterPage setPasswordInput(String password) {
    passwordInput.setValue(password);
    return this;
  }

  @Step("Ввести в поле Submit password: {0}")
  public RegisterPage setPasswordSubmitInput(String password) {
    passwordSubmitInput.setValue(password);
    return this;
  }

  @Step("Нажать кнопку Sign up")
  public RegisterPage submitRegistration() {
    signUpBtn.click();
    return this;
  }

  @Step("Нажать кнопку отображения Password")
  public RegisterPage openPassword() {
    passwordBtn.click();
    return this;
  }

  @Step("Поле Password отображается")
  public RegisterPage checkPasswordIsVisible() {
    openPassword().openSubmitPassword();
    passwordBtn.shouldHave(cssClass("form__password-button_active"));
    return this;
  }

  @Step("Нажать кнопку отображения Submit password")
  public RegisterPage openSubmitPassword() {
    passwordSubmitBtn.click();
    return this;
  }

  @Step("Поле Password отображается")
  public RegisterPage checkSubmitPasswordIsVisible() {
    openPassword().openSubmitPassword();
    passwordSubmitBtn.shouldHave(cssClass("form__password-button_active"));
    return this;
  }

  @Step("Проверить успешное сообщение о регистрации")
  public RegisterPage checkSuccessRegisterMessageIsVisible() {
    successMessageElement.shouldBe(visible);
    return this;
  }

  @Step("Проверить сообщение об ошибке регистрации")
  public RegisterPage checkErrorMessageIsVisible() {
    errorMessageElement.shouldBe(visible);
    return this;
  }

  @Step("Нажать на кнопку Sign In")
  public LoginPage clickSignInBtn() {
    signInBtn.click();
    return new LoginPage();
  }

  @Step("Проверка отображения о несовпадении паролей")
  public RegisterPage checkMessagePasswordAndSubmitPasswordNotEqual() {
    passwordEqualMessage.shouldBe(visible);
    return this;
  }
}