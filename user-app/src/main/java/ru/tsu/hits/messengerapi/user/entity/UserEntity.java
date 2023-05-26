package ru.tsu.hits.messengerapi.user.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;
import ru.tsu.hits.messengerapi.common.enumeration.Sex;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

/**
 * Сущность пользователя в системе.
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "_user")
public class UserEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;
    @Column(nullable = false)
    private String login;
    @Column(nullable = false)
    private String fullName;

    private LocalDate birthDate;

    @CreationTimestamp
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date creationDate;

    @Column(nullable = false)
    private String email;

    private String phoneNumber;

    private String city;

    private UUID avatar;

    private String password;

    @Enumerated(EnumType.STRING)
    private Sex sex;


    private UserEntity(String fullName, String city, String email, String login, String phoneNumber, LocalDate birthDate) {
        this.fullName = fullName;
        this.city = city;
        this.email = email;
        this.login = login;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
    }

    public static UserEntity from(String fullName, String city, String email, String login, String phoneNumber, LocalDate birthDate) {
        return new UserEntity(fullName, city, email, login, phoneNumber, birthDate);
    }

}
