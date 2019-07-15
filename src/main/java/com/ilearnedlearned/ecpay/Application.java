package com.ilearnedlearned.ecpay;

import org.springframework.boot.autoconfigure.*;

@EnableAutoConfiguration
@SpringBootApplication
@org.springframework.cache.annotation.EnableCaching
public class Application {

	public static void main(String[] args) {
		org.springframework.boot.SpringApplication.run(Application.class, args);
	}
}
