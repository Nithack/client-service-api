package com.nithack.client_service_api;

import org.springframework.boot.SpringApplication;

public class TestClientServiceApiApplication {

	public static void main(String[] args) {
		SpringApplication.from(ClientServiceApiApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
