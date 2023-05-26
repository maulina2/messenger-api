package ru.tsu.hits.messengerapi.user.util.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tsu.hits.messengerapi.user.repository.UserRepository;
import ru.tsu.hits.messengerapi.user.util.annotation.UniqueEmailValidation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Логика для проверки ограничения {@link UniqueEmailValidation}.
 * С проверяемым объектом типа {@link String}.
 */
@Component
@RequiredArgsConstructor
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmailValidation, String> {

    private final UserRepository userRepository;

    /**
     * Метод для проверки на уникальность почты пользователя.
     *
     * @param email   почта пользователя.
     * @param context контекст, в котором вычисляется ограничение.
     * @return если в БД уже есть пользователь с почтой {@code email}, то {@code false}, иначе {@code false}.
     */
    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return !userRepository.existsByEmail(email);
    }

}
