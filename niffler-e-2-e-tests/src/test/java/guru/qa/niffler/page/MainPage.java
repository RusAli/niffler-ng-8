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

  public void checkThatMainPageIsShown() {
    spendingTable.shouldBe(visible).shouldHave(innerText("History of Spendings"));
    statsSpinner.shouldBe(visible).shouldHave(innerText("Statistics"));
  }

}
