package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UsersApiClient;
import guru.qa.niffler.service.UsersClient;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import javax.annotation.Nullable;

import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;

public class UserExtension implements
        BeforeEachCallback,
        ParameterResolver {

  public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UserExtension.class);

  private static final String defaultPassword = "12345";
  private final UsersClient usersClient = new UsersApiClient();

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
            .ifPresent(userAnno -> {
              if ("".equals(userAnno.username())) {
                final String username = randomUsername();
                UserJson user = usersClient.createUser(
                        username,
                        defaultPassword
                );

                if (userAnno.incomeInvitations() > 0) {
                  usersClient.addIncomeInvitation(user, userAnno.incomeInvitations());
                }
                if (userAnno.outcomeInvitations() > 0) {
                  usersClient.addOutcomeInvitation(user, userAnno.outcomeInvitations());
                }
                if (userAnno.friends() > 0) {
                  usersClient.addFriend(user, userAnno.friends());
                }

                context.getStore(NAMESPACE).put(
                        context.getUniqueId(),
                        user.withPassword(
                                defaultPassword
                        )
                );
              }
            });
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext.getParameter().getType().isAssignableFrom(UserJson.class);
  }

  @Override
  public UserJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return createdUser();
  }

  public static @Nullable UserJson createdUser() {
    final ExtensionContext context = TestsMethodContextExtension.context();
    return context.getStore(NAMESPACE).get(context.getUniqueId(), UserJson.class);
  }
}
