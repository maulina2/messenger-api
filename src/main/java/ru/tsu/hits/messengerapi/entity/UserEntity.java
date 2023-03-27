package ru.tsu.hits.messengerapi.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import ru.tsu.hits.messengerapi.enumeration.Sex;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

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

    private String login;

    private String name;

    private Date birthDate;

    private Date creationDate;

    private String surname;

    private String patronymic;

    private String password;

    @Enumerated(EnumType.STRING)
    private Sex sex;
}
