package ru.tsu.hits.messengerapi.user.util.annotation;

import ru.tsu.hits.messengerapi.user.util.validator.CheckBirthDateValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;

/**
 * Валидирующая аннотация. Она нужна для проверки даты рождения пользователя.
 */
@Target({FIELD, PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = CheckBirthDateValidator.class)
public @interface CheckBirthDateValidation {
    String message() default "Дата рождения некорректна";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
