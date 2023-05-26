package ru.tsu.hits.messengerapi.common.annotation;

import ru.tsu.hits.messengerapi.common.validator.PageNumberValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;

/**
 * Валидирующая аннотация. Она нужна для проверки на данных для пагинации.
 */
@Target({FIELD, PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = PageNumberValidator.class)
public @interface PageNumberValidation {
    String message() default "Некорректный номер страницы";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
