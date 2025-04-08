package guru.qa.niffler.annotations;

import guru.qa.niffler.jupiter.CreateNewUserExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ExtendWith(CreateNewUserExtension.class)
public @interface NewRandomUser {
}