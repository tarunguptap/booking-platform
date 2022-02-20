package com.organization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class BookingPlatformApplication {
	public static void main(String[] args) {
		SpringApplication.run(BookingPlatformApplication.class, args);
	}
}
