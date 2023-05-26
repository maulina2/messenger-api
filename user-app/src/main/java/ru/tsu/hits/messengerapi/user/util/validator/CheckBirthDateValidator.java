package ru.tsu.hits.messengerapi.user.util.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tsu.hits.messengerapi.user.util.annotation.CheckBirthDateValidation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

/**
 * Логика для проверки ограничения {@link CheckBirthDateValidation}.
 * С проверяемым объектом типа {@link String}.
 */
@Component
@RequiredArgsConstructor
public class CheckBirthDateValidator implements ConstraintValidator<CheckBirthDateValidation, LocalDate> {

    /**
     * Метод для проверки на корректность даты рождения пользователя.
     */
    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
        return !date.isAfter(LocalDate.now());
    }
}
