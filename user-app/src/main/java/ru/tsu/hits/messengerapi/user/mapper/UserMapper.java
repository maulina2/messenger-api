package ru.tsu.hits.messengerapi.user.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.tsu.hits.messengerapi.common.dto.FullNameDto;
import ru.tsu.hits.messengerapi.common.dto.UserDto;
import ru.tsu.hits.messengerapi.commonsecurity.dto.JwtUserData;
import ru.tsu.hits.messengerapi.user.dto.SignUpDto;
import ru.tsu.hits.messengerapi.user.entity.UserEntity;

/**
 * Сервис для маппинга объектов типа UserEntity в объекты типа UserDto и JwtUserData.
 */
@Component
@RequiredArgsConstructor
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    /**
     * Преобразует объект типа UserEntity в объект типа UserDto.
     *
     * @param user объект типа UserEntity.
     * @return объект типа UserDto.
     */
    public UserDto userToUserDto(UserEntity user) {
        return new UserDto(
                user.getId(),
                user.getLogin(),
                user.getFullName(),
                user.getBirthDate(),
                user.getCreationDate(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getCity(),
                user.getAvatar(),
                user.getSex()
        );
    }

    /**
     * Преобразует объект типа UserEntity в объект типа JwtUserData.
     *
     * @param userEntity объект типа UserEntity.
     * @return объект типа JwtUserData.
     */
    public JwtUserData userToJwtUserData(UserEntity userEntity) {
        return new JwtUserData(
                userEntity.getId(),
                userEntity.getLogin(),
                userEntity.getFullName(),
                userEntity.getEmail());
    }

    /**
     * Преобразует объект типа SignUpDto в объект типа UserEntity.
     *
     * @param signUpDto объект типа SignUpDto.
     * @return объект типа UserEntity.
     */
    public UserEntity SignUpDtoToUser(SignUpDto signUpDto) {
        return UserEntity
                .builder()
                .fullName(signUpDto.getFullName())
                .avatar(null)
                .city(signUpDto.getCity())
                .phoneNumber(signUpDto.getPhoneNumber())
                .email(signUpDto.getEmail())
                .login(signUpDto.getLogin())
                .sex(signUpDto.getSex())
                .fullName(signUpDto.getFullName())
                .birthDate(signUpDto.getBirthDate())
                .password(passwordEncoder.encode(signUpDto.getPassword()))
                .build();
    }

    /**
     * Преобразует обьект типа UserEntity в обьект типа FullNameDto.
     * @param userEntity сущность пользователя в системе.
     * @return FullNameDto дто, в которой передается ФИО пользователя.
     */
    public FullNameDto userToFullNameDto(UserEntity userEntity) {
        return new FullNameDto(
                userEntity.getFullName());
    }

}