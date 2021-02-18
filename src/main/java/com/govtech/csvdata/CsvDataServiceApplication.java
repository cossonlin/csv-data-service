package com.govtech.csvdata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class CsvDataServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CsvDataServiceApplication.class, args);
	}

}
