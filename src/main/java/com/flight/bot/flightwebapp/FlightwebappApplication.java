package com.flight.bot.flightwebapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FlightwebappApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlightwebappApplication.class, args);
	}
}
