package ru.tsu.hits.messengerapi.user.util.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tsu.hits.messengerapi.user.repository.UserRepository;
import ru.tsu.hits.messengerapi.user.util.annotation.UniqueLoginValidation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


/**
 * Логика для проверки ограничения {@link UniqueLoginValidation}.
 * С проверяемым объектом типа {@link String}.
 */
@Component
@RequiredArgsConstructor
public class UniqueLoginValidator implements ConstraintValidator<UniqueLoginValidation, String>  {
    private final UserRepository userRepository;

    /**
     * Метод для проверки на уникальность почты пользователя.
     *
     * @param login логин пользователя.
     * @param context контекст, в котором вычисляется ограничение.
     * @return если в БД уже есть пользователь с почтой {@code email}, то {@code false}, иначе {@code false}.
     */
    @Override
    public boolean isValid(String login, ConstraintValidatorContext context) {
        return !userRepository.existsByLogin(login);
    }
}
