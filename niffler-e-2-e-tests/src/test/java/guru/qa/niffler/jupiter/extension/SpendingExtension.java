package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.SpendDbClient;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Date;

public class SpendingExtension implements BeforeEachCallback, ParameterResolver {

  public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(SpendingExtension.class);
//  private final SpendApiClient spendApiClient = new SpendApiClient();

  private final SpendDbClient spendDbClient = new SpendDbClient();

  @Override
  public void beforeEach(ExtensionContext context) {
    AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
            .ifPresent(anno -> {
              if (ArrayUtils.isEmpty(anno.spendings())) {
                return;
              }
              SpendJson spendJson = new SpendJson(
                      null,
                      new Date(),
                      new CategoryJson(
                              null,
                              anno.spendings()[0].category(),
                              anno.username(),
                              false
                      ),
                      anno.spendings()[0].currency(),
                      anno.spendings()[0].amount(),
                      anno.spendings()[0].description(),
                      anno.username()
              );

              SpendJson created = spendDbClient.createSpend(spendJson);
              context.getStore(NAMESPACE).put(context.getUniqueId(), created);
            });
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext.getParameter().getType().isAssignableFrom(SpendJson.class);
  }

  @Override
  public SpendJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return extensionContext.getStore(SpendingExtension.NAMESPACE).get(extensionContext.getUniqueId(), SpendJson.class);
  }
}
