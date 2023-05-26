package ru.tsu.hits.messengerapi.gatewayapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GatewayAppApplication {


    /**
     * Главный метод модуля gateway-app. Он собирает весь модуль.
     */
    public static void main(String[] args) {
        SpringApplication.run(GatewayAppApplication.class, args);
    }

}
