package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension.StaticUser;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.*;

@WebTest
public class FriendsWebTest {


  @Test
  void friendsTableShouldBeEmptyForNewUser(@UserType StaticUser user) {

    Selenide.open(LoginPage.URL, LoginPage.class)
            .doLogin(user.username(), user.password())
            .checkThatMainPageIsShown()
            .openFriends()
            .checkFriendTableIsEmpty();
  }

  @Test
  void friendShouldBePresentInFriendsTable(@UserType(value = WITH_FRIEND) StaticUser user) {

    Selenide.open(LoginPage.URL, LoginPage.class)
            .doLogin(user.username(), user.password())
            .checkThatMainPageIsShown()
            .openFriends()
            .checkFriendInTable(user.friend());
  }

  @Test
  void incomeInvitationBePresentInFriendsTable(@UserType(value = WITH_INCOME_REQUEST) StaticUser user) {

    Selenide.open(LoginPage.URL, LoginPage.class)
            .doLogin(user.username(), user.password())
            .checkThatMainPageIsShown()
            .openFriends()
            .checkRequestInTable(user.income());
  }

  @Test
  void outcomeInvitationBePresentInFriendsTable(@UserType(value = WITH_OUTCOME_REQUEST) StaticUser user) {

    Selenide.open(LoginPage.URL, LoginPage.class)
            .doLogin(user.username(), user.password())
            .checkThatMainPageIsShown()
            .openAll()
            .checkAllPeopleInTable(user.outcome());
  }

}