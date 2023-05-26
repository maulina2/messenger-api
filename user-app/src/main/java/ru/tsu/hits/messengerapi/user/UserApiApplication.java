package ru.tsu.hits.messengerapi.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.tsu.hits.messengerapi.commonsecurity.ImportSecurityAnnotation;

@SpringBootApplication
@ImportSecurityAnnotation
public class UserApiApplication {

	/**
	 * Главный метод модуля user-app. Он собирает весь модуль.
	 */
	public static void main(String[] args) {
		SpringApplication.run(UserApiApplication.class, args);
	}

}
