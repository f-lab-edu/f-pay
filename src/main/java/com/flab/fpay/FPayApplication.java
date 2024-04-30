package com.flab.fpay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class FPayApplication {

	public static void main(String[] args) {
		SpringApplication.run(FPayApplication.class, args);
	}
}