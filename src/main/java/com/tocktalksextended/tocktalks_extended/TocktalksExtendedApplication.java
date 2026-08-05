package com.tocktalksextended.tocktalks_extended;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import com.tocktalksextended.tocktalks_extended.price.config.KisApiProperties;

@SpringBootApplication
@EnableConfigurationProperties(KisApiProperties.class)
public class TocktalksExtendedApplication {

	public static void main(String[] args) {
		SpringApplication.run(TocktalksExtendedApplication.class, args);
	}

}
