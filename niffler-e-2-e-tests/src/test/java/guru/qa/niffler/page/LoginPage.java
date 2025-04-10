package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;

import static com.codeborne.selenide.Condition.innerText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {

  public static final String URL = Config.getInstance().loginUrl();

  private final SelenideElement usernameInput = $("input[name='username']");
  private final SelenideElement passwordInput = $("input[name='password']");
  private final SelenideElement submitBtn = $("button[type='submit']");
  private final SelenideElement header = $(".header");
  private final SelenideElement wrongCredentialsMessage = $(".form__error");

  public MainPage doLogin(String username, String password) {
    usernameInput.setValue(username);
    passwordInput.setValue(password);
    submitBtn.click();
    return new MainPage();
  }

  public LoginPage doLoginWithWrongCred(String username, String password) {
    usernameInput.setValue(username);
    passwordInput.setValue(password);
    submitBtn.click();
    return this;
  }

  public LoginPage checkThatLoginPageIsShown() {
    header.shouldBe(visible).shouldHave(innerText("Log in"));
    return this;
  }

  public LoginPage checkMessageWithWrongCredentials() {
    wrongCredentialsMessage.shouldBe(visible).shouldHave(innerText("Неверные учетные данные пользователя"));
    return this;
  }


}
