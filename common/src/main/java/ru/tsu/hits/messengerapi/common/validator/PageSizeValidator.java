package ru.tsu.hits.messengerapi.common.validator;

import lombok.RequiredArgsConstructor;
import ru.tsu.hits.messengerapi.common.annotation.PageSizeValidation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Логика для проверки ограничения {@link PageSizeValidation}.
 */

@RequiredArgsConstructor
public class PageSizeValidator implements ConstraintValidator<PageSizeValidation, Integer> {


    /**
     * Этот метод проверяет, является ли указанный номер страницы допустимым.
     *
     * @param pageSize  размер страницы для проверки
     * @param context    контекст, в котором выполняется проверка
     * @return true, если размер страницы больше нуля, в противном случае - false
     */
    @Override
    public boolean isValid(Integer pageSize, ConstraintValidatorContext context) {
        return pageSize > 0;
    }
}
