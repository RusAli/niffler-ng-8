package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.NewRandomUser;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

public class CreateNewUserExtension implements BeforeEachCallback, ParameterResolver {

  public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CreateNewUserExtension.class);

  @Override
  public void beforeEach(ExtensionContext context) {

    AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), NewRandomUser.class)
            .ifPresent(newUser -> {
              UserJson newUserJson = new UserJson(
                      null,
                      RandomDataUtils.randomUsername(),
                      RandomDataUtils.randomPassword(),
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