package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
public class FriendsTest {

  private static final Config CFG = Config.getInstance();

  @User(
          friends = 1
  )
  @Test
  void friendShouldBePresentInFriendsTable(UserJson user) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
            .successLogin(user.username(), user.testData().password())
            .checkThatPageLoaded()
            .searchFriend(user.username())
            .friendsPage()
            .checkExistingFriends(user.testData().friends().getFirst().username());
  }

  @User()
  @Test
  void friendsTableShouldBeEmptyForNewUser(UserJson user) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
            .successLogin(user.username(), user.testData().password())
            .checkThatPageLoaded()
            .friendsPage()
            .checkNoExistingFriends();
  }

  @User(
          incomeInvitations = 1
  )
  @Test
  void incomeInvitationBePresentInFriendsTable(UserJson user) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
            .successLogin(user.username(), user.testData().password())
            .checkThatPageLoaded()
            .searchFriend(String.valueOf(user.testData().friends().getFirst()))
            .friendsPage()
            .checkExistingInvitations(String.valueOf(user.testData().incomeInvitations().getFirst()));
  }

  @User(
          outcomeInvitations = 1
  )
  @Test()
  void outcomeInvitationBePresentInAllPeoplesTable(UserJson user) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
            .successLogin(user.username(), user.testData().password())
            .checkThatPageLoaded()
            .searchFriend(String.valueOf(user.testData().friends().getFirst()))
            .allPeoplesPage()
            .checkInvitationSentToUser(String.valueOf(user.testData().incomeInvitations().getFirst()));
  }
}
