package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.utils.RandomDataUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

public class CategoryExtension implements
        BeforeEachCallback,
        AfterTestExecutionCallback,
        ParameterResolver {

  public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);

//  private final SpendApiClient spendApiClient = new SpendApiClient();

  private final SpendDbClient spendDbClient = new SpendDbClient();

  @Override
  public void beforeEach(ExtensionContext context) {
    AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
            .ifPresent(anno -> {
              if (ArrayUtils.isEmpty(anno.categories())) {
                return;
              }
              CategoryJson category = new CategoryJson(
                      null,
                      RandomDataUtils.randomCategory(),
                      anno.username(),
                      anno.categories()[0].archived()
              );

              CategoryJson created = spendDbClient.createCategory(category);
              if (anno.categories()[0].archived()) {
                CategoryJson archivedCategory = new CategoryJson(
                        created.id(),
                        created.name(),
                        created.username(),
                        true
                );
                created = spendDbClient.updateCategory(archivedCategory);
              }

              context.getStore(NAMESPACE).put(
                      context.getUniqueId(),
                      created
              );
            });
  }

  @Override
  public void afterTestExecution(ExtensionContext context) {
    CategoryJson category = context.getStore(NAMESPACE).get(context.getUniqueId(), CategoryJson.class);
    if (!category.archived()) {
      category = new CategoryJson(
              category.id(),
              category.name(),
              category.username(),
              true
      );
      spendDbClient.updateCategory(category);
    }
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext.getParameter().getType().isAssignableFrom(CategoryJson.class);
  }

  @Override
  public CategoryJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), CategoryJson.class);
  }
}
