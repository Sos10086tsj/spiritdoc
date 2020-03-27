package com.dreamferry.spiritdoc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class SpiritApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpiritApplication.class, args);
	}
}
