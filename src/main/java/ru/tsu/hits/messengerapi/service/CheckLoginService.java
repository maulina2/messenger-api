package ru.tsu.hits.messengerapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tsu.hits.messengerapi.exception.ConflictException;
import ru.tsu.hits.messengerapi.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CheckLoginService {

    private final UserRepository userRepository;

    /**
     * Метод для проверки существования пользователя с заданным логином.
     *
     * @param login логин.
     * @throws ConflictException выбрасывается, если заданный логин уже используется.
     */
    public void existsByLogin(String login) {
        if (userRepository.existsByLogin(login)) {
            throw new ConflictException("Пользователь с таким логином уже существует");
        }
    }

}
