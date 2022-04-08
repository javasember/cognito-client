package com.gys.cognitoclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CognitoClientApplication {

	public static void main(String[] args) {

		var ctx = SpringApplication.run(CognitoClientApplication.class, args);

		ctx.getBean(Client.class)
			.delete("username");
	}


	@Bean
	@ConfigurationProperties(prefix = "cognito.client")
	public CognitoConfiguration cognitoConfiguration() {
		return new CognitoConfiguration();
	}
}
