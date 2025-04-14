package guru.qa.niffler.jupiter;

import com.github.javafaker.Faker;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.model.CategoryJson;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

public class CategoryExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

  public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);
  private final SpendApiClient spendApiClient = new SpendApiClient();

  @Override
  public void beforeEach(ExtensionContext context) {

    AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), Category.class)
            .ifPresent(anno -> {
              Faker faker = new Faker();
              CategoryJson categoryJson = new CategoryJson(
                      null,
                      faker.commerce().productName(),
                      anno.username(),
                      false);
              CategoryJson createdCategory = spendApiClient.addCategory(categoryJson);
              context.getStore(NAMESPACE).put(context.getUniqueId(), createdCategory);

              if (anno.archived()) {
                CategoryJson archivedCategory = new CategoryJson(
                        createdCategory.id(),
                        createdCategory.name(),
                        createdCategory.username(),
                        true);
                createdCategory = spendApiClient.updateCategory(archivedCategory);
                context.getStore(NAMESPACE).put(context.getUniqueId(), createdCategory);
              }
            });
  }

  @Override
  public void afterEach(ExtensionContext context) {
    CategoryJson createdCategory = context.getStore(NAMESPACE).get(context.getUniqueId(), CategoryJson.class);
    CategoryJson categoryToArchive = new CategoryJson(
            createdCategory.id(),
            createdCategory.name(),
            createdCategory.username(),
            true
    );
    spendApiClient.updateCategory(categoryToArchive);
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext.getParameter().getType().isAssignableFrom(CategoryJson.class);
  }

  @Override
  public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), CategoryJson.class);
  }
}