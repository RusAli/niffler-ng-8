package guru.qa.niffler.jupiter;

import com.github.javafaker.Faker;
import guru.qa.niffler.jupiter.annotation.NewRandomUser;
import guru.qa.niffler.model.UserJson;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

public class CreateNewUserExtension implements BeforeEachCallback, ParameterResolver {

  public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CreateNewUserExtension.class);

  @Override
  public void beforeEach(ExtensionContext context) {

    AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), NewRandomUser.class)
            .ifPresent(newUser -> {
              Faker faker = new Faker();
              UserJson newUserJson = new UserJson(
                      null,
                      faker.name().username(),
                      faker.internet().password(3, 12),
                      null,
                      null,
                      null
              );

              context.getStore(NAMESPACE).put(context.getUniqueId(), newUserJson);
            });
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext.getParameter().getType().isAssignableFrom(UserJson.class);
  }

  @Override
  public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return extensionContext.getStore(CreateNewUserExtension.NAMESPACE).get(extensionContext.getUniqueId(), UserJson.class);
  }
}