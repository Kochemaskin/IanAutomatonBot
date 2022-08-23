package com.proj3ct.IanAutomatonBot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class IanAutomatonBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(IanAutomatonBotApplication.class, args);
	}

}
