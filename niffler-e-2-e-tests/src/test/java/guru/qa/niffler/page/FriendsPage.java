package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class FriendsPage {

  public static final String URL = Config.getInstance().frontUrl() + "people/friends";

  private final SelenideElement friendsTable = $("#friends");
  private final SelenideElement allPeopleTable = $("#all");
  private final SelenideElement requestsTable = $("#requests");


  public FriendsPage checkFriendTableIsEmpty() {
    friendsTable.shouldNot(visible);
    return this;
  }

  public FriendsPage checkFriendInTable(String friendName) {
    friendsTable.$$("tr").find(text(friendName)).shouldBe(visible);
    return this;
  }

  public FriendsPage checkRequestInTable(String friendName) {
    requestsTable.$$("tr").find(text(friendName)).shouldBe(visible);
    return this;
  }

  public FriendsPage checkAllPeopleInTable(String friendName) {
    allPeopleTable.$$("tr").find(text(friendName)).shouldBe(visible);
    return this;
  }
}