package ru.tsu.hits.messengerapi.common.validator;

import lombok.RequiredArgsConstructor;
import ru.tsu.hits.messengerapi.common.annotation.PageNumberValidation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Логика для проверки ограничения {@link PageNumberValidation}.
 */

@RequiredArgsConstructor
public class PageNumberValidator implements ConstraintValidator<PageNumberValidation, Integer> {

    /**
     * Этот метод проверяет, является ли указанный номер страницы допустимым.
     *
     * @param pageNumber номер страницы для проверки
     * @param context    контекст, в котором выполняется проверка
     * @return true, если номер страницы больше или равен нулю, в противном случае - false
     */
    @Override
    public boolean isValid(Integer pageNumber, ConstraintValidatorContext context) {
        return pageNumber >= 0;
    }
}
