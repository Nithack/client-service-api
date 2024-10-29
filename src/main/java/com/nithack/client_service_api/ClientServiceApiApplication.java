package com.nithack.client_service_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource(value = "file:.env", ignoreResourceNotFound = true)
public class ClientServiceApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClientServiceApiApplication.class, args);
	}

}
