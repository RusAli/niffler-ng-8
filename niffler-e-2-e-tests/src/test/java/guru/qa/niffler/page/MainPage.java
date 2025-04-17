package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class MainPage {

  private final ElementsCollection tableRows = $$("#spendings tbody tr");
  private final SelenideElement spendingTable = $("#spendings");
  private final SelenideElement statsSpinner = $("#stat");
  private final SelenideElement header = $("#root header");
  private final SelenideElement iconBtn = header.$("button");
  private final SelenideElement iconMenu = $("ul[role='menu']");

  public EditSpendingPage editSpending(String spendingDescription) {
    tableRows.find(text(spendingDescription))
            .$$("td")
            .get(5)
            .click();
    return new EditSpendingPage();
  }

  public void checkThatTableContains(String spendingDescription) {
    tableRows.find(text(spendingDescription))
            .should(visible);
  }

  public MainPage checkThatMainPageIsShown() {
    spendingTable.shouldBe(visible).shouldHave(innerText("History of Spendings"));
    statsSpinner.shouldBe(visible).shouldHave(innerText("Statistics"));
    return this;
  }

  public FriendsPage openFriends() {
    iconBtn.click();
    iconMenu.$$("li").find(text("Friends")).click();
    return new FriendsPage();
  }

  public FriendsPage openAll() {
    iconBtn.click();
    iconMenu.$$("li").find(text("All People")).click();
    return new FriendsPage();
  }
}
